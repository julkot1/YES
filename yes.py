from enum import Enum
import subprocess
import sys


class StatementTokens(Enum):
    PUSH = 1
    ECHO = 2
    ADD = 3


class ArgTokens(Enum):
    VALUE = 0
    STATEMENT = 1
    REGISTER = 2


def compile(inFile):
    with open("out.c", "w") as out:
        out.write("#include <stdio.h>\n#include <stdlib.h>\n")
        out.write("#define PTG_SIZE 300000\n#define PTX_SIZE 50000\n")
        out.write("int main()\n{\n")
        out.write("    int *gr = (int *)malloc(PTG_SIZE * sizeof(int));\n")
        out.write("    int *xr = (int *)malloc(PTX_SIZE * sizeof(int));\n")
        out.write("    int ptr = 0, ptx = 0;\n")
        #

        #
        out.write("    free(xr);\n")
        out.write("    free(gr);\n")
        out.write("    return 0;\n}")
        out.close()


def help():
    print('[file_name] flags')
    print('compile yes program')
    print('flags:')
    print('     run  -r')


def main():
    argv = sys.argv
    print(len(argv))
    if len(argv) >= 2:
        print('[PYTHON] making C file')
        compile(argv[1])

        if len(argv) == 3:
            if argv[2] == '-r':
                print('[PYTHON] running CMD')
                subprocess.run(
                    './run.sh ', shell=True)
    else:
        help()


if __name__ == "__main__":
    main()
