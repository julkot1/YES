from enum import Enum
from io import TextIOWrapper
import subprocess
import sys

from lexer import Register, Statement, Value, lexFile, REGISTERS_TOKENS, StatementTokens, createAST


class ArgTokens(Enum):
    VALUE = 0
    STATEMENT = 1
    REGISTER = 2


def getArg(arg, out: TextIOWrapper):
    if isinstance(arg, Value):
        return arg.token
    elif isinstance(arg, Statement):
        writeStatement(arg, out)
        return '*(xr + ptx - 1)'
    elif isinstance(arg, Register):
        if arg.index == None:
            return '*({r} + pt{pt} - 1)'.format(r=arg.token, pt=arg.token[0])
        if isinstance(arg.index, Value):
            return '*({r} + pt{pt} - {a})'.format(r=arg.token, pt=arg.token[0], a=int(arg.index.token)+1)
        writeStatement([arg.index], out)
        return '*({r} + pt{pt} - *(xr+ptx-1) -1)'.format(r=arg.token, pt=arg.token[0])


def putToCr(token, out: TextIOWrapper):
    value = getArg(token, out)
    out.write(
        f"*(cr + ptc) = {value}; ptc++;")


def writeOperation(statement: Statement, out: TextIOWrapper):
    if statement.token == StatementTokens.PUSH.value:
        assert len(statement.args) == 1, "Invalid arguments in PUSH statement"
        out.write(f"*(gr + ptg) = *cr;")
        out.write("ptg++;")
    elif statement.token == StatementTokens.ADD.value:
        assert len(statement.args) == 2, "Invalid arguments in ADD statement"
        out.write(f"*(xr + ptx) = *cr + *(cr+1);")
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
        assert len(statement.args) == 2, "Invalid arguments in ADD statement"
        out.write(f"*(xr + ptx) = *cr / *(cr+1);")
        out.write("ptx++;")
    elif statement.token == StatementTokens.MOD.value:
        assert len(statement.args) == 2, "Invalid arguments in ADD statement"
        out.write(f"*(xr + ptx) = *cr * *(cr+1);")
        out.write("ptx++;")
    elif statement.token == StatementTokens.ECHO.value:
        # TODO: write instead of printf
        out.write(f'printf("%d\\n", *cr);')
    else:
        assert False, "not existing statement"


def writeStatement(statement: Statement, out: TextIOWrapper):
    out.write("{")
    out.write(
        "int * cr = (int * )malloc({size} * sizeof(int));int ptc=0;".format(size=len(statement.args)))
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
        out.write("int *gr = (int *)malloc(PTG_SIZE * sizeof(int));")
        out.write("int *xr = (int *)malloc(PTX_SIZE * sizeof(int));")
        out.write("int ptg = 0, ptx = 0;")
        #
        for statement in stack:
            writeStatement(statement, out)
        #
        out.write("free(xr);")
        out.write("free(gr);")
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
