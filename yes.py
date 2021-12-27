from enum import Enum
from io import TextIOWrapper
import subprocess
import sys

from pyparsing import QuotedString

from lexer import Register, Statement, Value, lexFile, SyntaxTokens, StatementTokens, createAST, BOOLEAN_TOKENS, printAST

MATH_STATEMENTS = [StatementTokens.ADD.value, StatementTokens.SUB.value,
                   StatementTokens.DIV.value, StatementTokens.MUL.value, StatementTokens.MOD.value]
BIN_STATEMENTS = [StatementTokens.BAND.value, StatementTokens.LSHIFT.value,
                  StatementTokens.BOR.value, StatementTokens.RSHIFT.value, StatementTokens.BNOT.value, StatementTokens.XOR.value]


class Types(Enum):
    #(token, id)
    CHAR = 'Char'
    SHORT = 'Short'
    INT = 'Int'
    LONG = 'Long'
    FLOAT = 'Float'
    BOOLEAN = 'Bool'
    STR = 'Str'
    TYPE = 'Type'

    @classmethod
    def isIn(self, value):
        return value in [val for val in self._value2member_map_.keys()]


def getCType(yType: str):
    if yType == Types.STR.value:
        return "char *"
    if yType == Types.CHAR.value or yType == Types.TYPE.value:
        return "unsigned char"
    return yType.lower()


def isYType(token: str):
    if Types.isIn(token):
        return token
    return False


def isYStr(token: str):
    qs = QuotedString(SyntaxTokens.STRING.value).searchString(token)
    if len(qs) == 0:
        return False
    else:
        assert len(qs) == 1, "Invalid Str quote"
        return token


def isYBool(token: str):
    if token in BOOLEAN_TOKENS:
        return token
    return False


def isYFloat(token: str):
    try:
        float(token)
        return token
    except:
        return False


def isYIntegers(token: str, minVal: int, maxVal: int):
    try:
        v = 0
        if len(token) > 2:
            if token[:2:] == '0b':
                v = int(token, 2)
            elif token[:2:] == '0x':
                v = int(token, 16)
            elif token[1] == '0':
                v = int(token, 8)
            else:
                v = int(token)
        else:
            v = int(token)
        if v >= minVal and v <= maxVal:
            return token
        return False
    except:
        return False


def isYChar(token: str):
    return isYIntegers(token, 0, 255)


def isYShort(token: str):
    return isYIntegers(token, -2**15, 2**15-1)


def isYInt(token: str):
    return isYIntegers(token, -2**31, 2**31-1)


def isYLong(token: str):
    return isYIntegers(token, -2**63, 2**63-1)


def getType(val: Value):
    if isYStr(val.token):
        return Types.STR
    if isYType(val.token):
        return Types.TYPE
    if isYBool(val.token):
        return Types.BOOLEAN
    if isYChar(val.token):
        return Types.CHAR
    if isYShort(val.token):
        return Types.SHORT
    if isYInt(val.token):
        return Types.INT
    if isYLong(val.token):
        return Types.LONG
    if isYFloat(val.token):
        return Types.FLOAT
    return None


def getStatementType(statement: Statement):

    if statement.type != None:
        return statement.type
    if statement.token in MATH_STATEMENTS:
        return getMathStatementType([arg.type for arg in statement.args])
    if statement.token in BIN_STATEMENTS:
        return getBinStatementType([arg.type for arg in statement.args])


def getBinStatementType(argsType: list):
    if Types.LONG.value in argsType:
        return Types.LONG.value
    if Types.INT.value in argsType:
        return Types.INT.value
    if Types.SHORT.value in argsType:
        return Types.SHORT.value
    return Types.CHAR.value


def getMathStatementType(argsType: list):
    if Types.FLOAT.value in argsType:
        return Types.FLOAT.value
    if Types.LONG.value in argsType:
        return Types.LONG.value
    if Types.INT.value in argsType:
        return Types.INT.value
    if Types.SHORT.value in argsType:
        return Types.SHORT.value
    return Types.CHAR.value


def getArg(arg, out: TextIOWrapper):
    if isinstance(arg, Value):
        prevType = arg.type
        arg.type = getType(arg).value
        if arg.type == None:
            arg.type = prevType
        return (arg.token, getCType(arg.type))
    elif isinstance(arg, Statement):
        cType = getCType(writeStatement(arg, out))
        return (f'*(({cType}*)xr[ptx - 1])', cType)
    elif isinstance(arg, Register):
        if arg.index == None:
            return ('*(({cType}*){r}[pt{pt} - 1])'.format(r=arg.token, pt=arg.token[0], cType=getCType(arg.type)), getCType(arg.type))
        if isinstance(arg.index, Value):
            return ('*(({cType}*){r}[ pt{pt} - {a}])'.format(r=arg.token, pt=arg.token[0], a=int(arg.index.token)+1, cType=getCType(arg.type)), getCType(arg.type))
        writeStatement([arg.index], out)
        return ('*(({cType}*){r}[pt{pt} - *(xr+ptx-1) -1])'.format(r=arg.token, pt=arg.token[0], cType=getCType(arg.type)), getCType(arg.type))


def putToCr(token, out: TextIOWrapper):
    value, yType = getArg(token, out)
    out.write(
        f"*(cr + ptc) = malloc(sizeof({yType}));")
    out.write(
        f"*(({yType} *)cr[ptc]) = {value}; ptc++;")


def writeMathOperation(statement: Statement, out: TextIOWrapper, operator: str):
    argsRawType = [statement.args[0].type, statement.args[1].type]
    yType = getCType(statement.type)
    argsType = [getCType(argsRawType[0]), getCType(argsRawType[1])]
    out.write(f"xr[ptx] = malloc(sizeof({yType}));")
    out.write(
        f"*(({yType} *)xr[ptx]) = *(({argsType[0]}*)cr[0]) {operator} *(({argsType[1]}*)cr[1]);")
    out.write("ptx++;")


def writeOperation(statement: Statement, out: TextIOWrapper):

    if statement.token == StatementTokens.PUSH.value:
        assert len(statement.args) == 1, "Invalid arguments in PUSH statement"
        yType = getCType(statement.args[0].type)
        out.write(f"gr[ptg] = malloc(sizeof({yType}));")
        out.write(f"*(({yType}*)gr[ptg]) = *(({yType}*)cr[0]);")
        out.write("ptg++;")
    elif statement.token == StatementTokens.ADD.value:
        assert len(statement.args) == 2, "Invalid arguments in ADD statement"
        writeMathOperation(statement, out, '+')
    elif statement.token == StatementTokens.SUB.value:
        assert len(statement.args) == 2, "Invalid arguments in SUB statement"
        writeMathOperation(statement, out, '-')
    elif statement.token == StatementTokens.MUL.value:
        assert len(statement.args) == 2, "Invalid arguments in MUL statement"
        writeMathOperation(statement, out, '*')
    elif statement.token == StatementTokens.DIV.value:
        assert len(statement.args) == 2, "Invalid arguments in DIV statement"
        writeMathOperation(statement, out, '/')
    elif statement.token == StatementTokens.MOD.value:
        assert len(statement.args) == 2, "Invalid arguments in MOD statement"
        writeMathOperation(statement, out, '%')
    elif statement.token == StatementTokens.LSHIFT.value:
        assert len(statement.args) == 2, "Invalid arguments in LSHIFT statement"
        writeMathOperation(statement, out, '<<')
    elif statement.token == StatementTokens.RSHIFT.value:
        assert len(statement.args) == 2, "Invalid arguments in RSHIFT statement"
        writeMathOperation(statement, out, '>>')
    elif statement.token == StatementTokens.BOR.value:
        assert len(statement.args) == 2, "Invalid arguments in bOR statement"
        writeMathOperation(statement, out, '|')
    elif statement.token == StatementTokens.BAND.value:
        assert len(statement.args) == 2, "Invalid arguments in BAND statement"
        writeMathOperation(statement, out, '&')
    elif statement.token == StatementTokens.BNOT.value:
        assert len(statement.args) == 2, "Invalid arguments in BNOT statement"
        writeMathOperation(statement, out, '~')
    elif statement.token == StatementTokens.XOR.value:
        assert len(statement.args) == 2, "Invalid arguments in XOR statement"
        writeMathOperation(statement, out, '^')
    elif statement.token == StatementTokens.ECHO.value:
        assert len(statement.args) >= 2, "Invalid arguments in ECHO statement"
        def argType(num): return getCType(statement.args[num].type)
        args = ', '.join(
            "*(({t}*)cr[{el}])".format(t=argType(el), el=el)for el in range(len(statement.args)))
        out.write(f'char buffer[strlen(*((char **)cr[0]))];')
        out.write(f'sprintf(buffer, {args});')
        out.write(f'printf("%s", buffer);')
    else:
        pass
        print(statement.token)
        assert False, "not existing statement"


def writeStatement(statement: Statement, out: TextIOWrapper):
    statement.type = getStatementType(statement)
    out.write("{")
    out.write(
        "void **cr = malloc({size} * sizeof(void *));int ptc=0;".format(size=len(statement.args)))
    if len(statement.args) >= 1:
        for t in statement.args:
            putToCr(t, out)
    writeOperation(statement, out)
    out.write("free(cr);};")

    return statement.type


def compile(stack):
    with open("out.c", "w") as out:
        out.write(
            "#include <stdio.h>\n#include <stdlib.h>\n#include<unistd.h>\n#include <string.h>\n")
        out.write("#define PTG_SIZE 300000\n#define PTX_SIZE 50000\n")
        out.write("int main()\n{")
        out.write("void **gr = malloc(PTG_SIZE * sizeof(void *));")
        out.write("void **xr = malloc(PTX_SIZE * sizeof(void *));")
        out.write("int ptg = 0, ptx = 0;")
        #
        for statement in stack:
            writeStatement(statement, out)
        #
        out.write(
            "for (int i = 0; i < PTX_SIZE; i++){free(*(xr+i));}free(xr);")
        out.write(
            "for (int i = 0; i < PTG_SIZE; i++){free(*(gr+i));}free(gr);")
        out.write("return 0;}")
        out.close()


def help():
    print('[file_name] flags')
    print('compile yes program')
    print('flags:')
    print('     run  -r')


def main():
    argv = sys.argv
    if len(argv) >= 2:
        print('[PYTHON] gererate ast')
        file = lexFile(argv[1])
        ast = createAST(file[1])
        print('[PYTHON] gererate C file')
        compile(ast)
        if len(argv) == 3:
            if argv[2] == '-r':
                subprocess.run(
                    './run.sh ', shell=True)
    else:
        help()


if __name__ == "__main__":
    main()
