from enum import Enum
from io import TextIOWrapper
import subprocess
import sys

from lexer import lexFile, REGISTERS_TOKENS, StatementTokens


class ArgTokens(Enum):
    VALUE = 0
    STATEMENT = 1
    REGISTER = 2


def getToken(token, out: TextIOWrapper):
    if isinstance(token, str):
        if token in REGISTERS_TOKENS:
            return '*({r} + pt{pt} - 1)'.format(r=token, pt=token[0])
        else:
            return token
    else:
        if token[0] in REGISTERS_TOKENS:
            if len(token) == 1:
                return '*({r} + pt{pt} - 1)'.format(r=token[0], pt=token[0][0])
            print(token[1][0])
            if token[1][0] in [item.value for item in StatementTokens]:
                writeTokens([token[1]], out)
                return '*({r} + pt{pt} - *(xr+ptx-1))'.format(r=token[0], pt=token[0][0])
            return '*({r} + pt{pt} - {a})'.format(r=token[0], pt=token[0][0], a=int(token[1][0])+1)

        else:
            writeTokens(token, out)
            return '*(xr + ptx - 1)'


def putToCr(token, out: TextIOWrapper):
    value = getToken(token, out)
    out.write(
        f"*(cr + ptc) = {value}; ptc++;")


def writeStatement(token: list, out: TextIOWrapper):
    if token[0] == StatementTokens.PUSH.value:
        assert len(token) == 2, "Invalid arguments in PUSH statement"
        out.write(f"*(gr + ptg) = *cr;")
        out.write("ptg++;")
    elif token[0] == StatementTokens.ADD.value:
        assert len(token) == 3, "Invalid arguments in ADD statement"
        out.write(f"*(xr + ptx) = *cr + *(cr+1);")
        out.write("ptx++;")
    elif token[0] == StatementTokens.SUB.value:
        assert len(token) == 3, "Invalid arguments in SUB statement"
        out.write(f"*(xr + ptx) = *cr - *(cr+1);")
        out.write("ptx++;")
    elif token[0] == StatementTokens.MUL.value:
        assert len(token) == 3, "Invalid arguments in MUL statement"
        out.write(f"*(xr + ptx) = *cr * *(cr+1);")
        out.write("ptx++;")
    elif token[0] == StatementTokens.DIV.value:
        assert len(token) == 3, "Invalid arguments in ADD statement"
        out.write(f"*(xr + ptx) = *cr / *(cr+1);")
        out.write("ptx++;")
    elif token[0] == StatementTokens.MOD.value:
        assert len(token) == 3, "Invalid arguments in ADD statement"
        out.write(f"*(xr + ptx) = *cr * *(cr+1);")
        out.write("ptx++;")
    elif token[0] == StatementTokens.ECHO.value:
        # TODO: write instead of printf
        out.write(f'printf("%d\\n", *cr);')
    else:
        assert False, token[0]+" is not existing statement"


def writeTokens(tokens: list, out: TextIOWrapper):
    for index, token in enumerate(tokens):
        out.write("{")
        out.write(
            "int * cr = (int * )malloc({size} * sizeof(int));int ptc=0;".format(size=len(token)))
        if len(token) > 1:
            for t in token[1::]:
                putToCr(t, out)
        writeStatement(token, out)
        out.write("free(cr);};")


def compile(code: tuple):
    imports, tokens = code
    with open("out.c", "w") as out:
        out.write("#include <stdio.h>\n#include <stdlib.h>\n#include<unistd.h>\n")
        out.write("#define PTG_SIZE 300000\n#define PTX_SIZE 50000\n")
        out.write("int main()\n{")
        out.write("int *gr = (int *)malloc(PTG_SIZE * sizeof(int));")
        out.write("int *xr = (int *)malloc(PTX_SIZE * sizeof(int));")
        out.write("int ptg = 0, ptx = 0;")
        #
        writeTokens(tokens, out)
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
    argv = sys.argv
    if len(argv) >= 2:
        print('[PYTHON] gererating C file')
        compile(lexFile(argv[1]))
        if len(argv) == 3:
            if argv[2] == '-r':
                print('[PYTHON] running CMD')
                subprocess.run(
                    './run.sh ', shell=True)
    else:
        help()


if __name__ == "__main__":
    main()
