from enum import Enum
from io import TextIOWrapper
import subprocess
import sys

from pyparsing import QuotedString

from lexer import Register, Statement, Value, lexFile, SyntaxTokens, StatementTokens, createAST, BOOLEAN_TOKENS, printAST


class Types(Enum):
    #(token, id)
    CHAR = 'Char'
    SHORT = 'Short'
    INT = 'Int'
    LONG = 'Long'
    FLOAT = 'Float'
    DOUBLE = 'Double'
    BOOLEAN = 'Bool'
    STR = 'Str'


def getCType(yType: str):
    if yType == Types.STR.value:
        return "char *"
    if yType == Types.CHAR.value:
        return "unsigned char"
    return yType.lower()


def isYstr(token: str):
    qs = QuotedString(SyntaxTokens.STRING.value).searchString(token)
    print(qs, "-")
    if qs == []:
        return False
    assert len(qs) == 1, "Invalid Str quote"
    assert "{q}{text}{q}".format(
        q=SyntaxTokens.STRING.value, text=qs[0][0]) == token, "Invalid Str quote"
    return token


def isYbool(token: str):
    if token in BOOLEAN_TOKENS:
        return token
    return False


def isYchar(token: str):
    if token.isdigit():
        if int(token) in range(0, 255):
            return int(token)
    quotedChar = isYstr(token)
    if quotedChar:
        if len(quotedChar) == 3:
            return ord(quotedChar[1])
    return False


def getType(val: Value):
    pass


def getMathStatementType(argsType: list):
    if Types.DOUBLE.value in argsType:
        return Types.DOUBLE.value
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
        return (arg.token, getCType(arg.type))
    elif isinstance(arg, Statement):
        writeStatement(arg, out)
        return '*(xr + ptx - 1)'
    elif isinstance(arg, Register):
        if arg.index == None:
            return ('*({r} + pt{pt} - 1)'.format(r=arg.token, pt=arg.token[0]), getCType(arg.type))
        if isinstance(arg.index, Value):
            return ('*({r} + pt{pt} - {a})'.format(r=arg.token, pt=arg.token[0], a=int(arg.index.token)+1), getCType(arg.type))
        writeStatement([arg.index], out)
        return ('*({r} + pt{pt} - *(xr+ptx-1) -1)'.format(r=arg.token, pt=arg.token[0]), getCType(arg.type))


def putToCr(token, out: TextIOWrapper):
    value, yType = getArg(token, out)
    out.write(
        f"*(cr + ptc) = malloc(sizeof({yType}));")
    out.write(
        f"*(({yType} *)cr + ptc) = {value}; ptc++;")


def writeOperation(statement: Statement, out: TextIOWrapper):
    if statement.token == StatementTokens.PUSH.value:
        assert len(statement.args) == 1, "Invalid arguments in PUSH statement"
        yType = getCType(statement.args[0].type)
        out.write(f"gr[ptg] = malloc(sizeof({yType}));")
        out.write(f"*(({yType}*)gr[ptg]) = *(({yType}*)cr);")
        out.write("ptg++;")
    elif statement.token == StatementTokens.ADD.value:
        assert len(statement.args) == 2, "Invalid arguments in ADD statement"
        argsRawType = [statement.args[0].type, statement.args[1].type]
        yType = getCType(getMathStatementType(argsRawType))
        argsType = [getCType(argsRawType[0]), getCType(argsRawType[1])]
        out.write(f"xr[ptx] = malloc(sizeof({yType}));")
        out.write(
            f"*(({yType} *)xr[ptx]) = *(({argsType[0]}*)cr) + *(({argsType[1]}*)cr + 1);")
        out.write("ptx++;")
    elif statement.token == StatementTokens.SUB.value:
        assert len(statement.args) == 2, "Invalid arguments in SUB statement"
        out.write(f"*(xr + ptx) = *cr - *(cr+1);")
        out.write("ptx++;")
    elif statement.token == StatementTokens.MUL.value:
        assert len(statement.args) == 2, "Invalid arguments in MUL statement"
        out.write(f"*(xr + ptx) = *cr * *(cr+1);")
        out.write("ptx++;")
    elif statement.token == StatementTokens.DIV.value:
        assert len(statement.args) == 2, "Invalid arguments in DIV statement"
        out.write(f"*(xr + ptx) = *cr / *(cr+1);")
        out.write("ptx++;")
    elif statement.token == StatementTokens.MOD.value:
        assert len(statement.args) == 2, "Invalid arguments in MOD statement"
        out.write(f"*(xr + ptx) = *cr * *(cr+1);")
        out.write("ptx++;")
    elif statement.token == StatementTokens.ECHO.value:
        # TODO: write instead of printf
        out.write(f'printf("%d\\n", *((int *)(*cr)));')
    else:
        pass
        print(statement.token)
        assert False, "not existing statement"


def writeStatement(statement: Statement, out: TextIOWrapper):
    out.write("{")
    out.write(
        "void **cr = malloc({size} * sizeof(void *));int ptc=0;".format(size=len(statement.args)))
    if len(statement.args) >= 1:
        for t in statement.args:
            putToCr(t, out)
    writeOperation(statement, out)
    out.write("free(cr);};")


def compile(stack):
    with open("out.c", "w") as out:
        out.write("#include <stdio.h>\n#include <stdlib.h>\n#include<unistd.h>\n")
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
    file = lexFile("program.yes")
    ast = createAST(file[1])
    compile(ast)
    argv = sys.argv
    if len(argv) >= 2:
        print('[PYTHON] gererating C file')
        file = lexFile(argv[1])
        ast = createAST(file[1])
        compile(ast)
        if len(argv) == 3:
            if argv[2] == '-r':
                print('[PYTHON] running CMD')
                subprocess.run(
                    './run.sh ', shell=True)
    else:
        help()


if __name__ == "__main__":
    main()
