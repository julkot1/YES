from re import I
from Yes import Statement, Tokens, splitArray
from pyparse import nestedExpr
from version import YESman, YES


def readFile(name):
    codeFile = open(name)
    statemant_line = ''
    for line in codeFile:
        newLine = line.strip().replace('\n', ' ')
        if newLine.endswith(';'):
            index = len(newLine)-1
            newLine = newLine[:index]+' '+newLine[index:]+' '
        statemant_line+=newLine
    return statemant_line
def interpretLines(code):
    stack = []
    arr = splitArray(nestedExpr('{','}').parseString('{'+code+'}').asList()[0], ';')
    for a in arr:
        newArr = []
        for o in a:
            if isinstance(o,list) and o[0] != Tokens.SCOPE_BODY.value:
                sc = splitArray(o, ';')
                if len(sc)==1:
                    newArr.append(sc[0])
                else:
                    newArr.append(sc)
            else:
                newArr.append(o)
        stack.append(Statement(newArr))
    return stack
def executeStack(stack):
    for el in stack:
        el.execute()

def main():
    print('YESman interpreter version ('+YESman+'), YES version('+YES+')')
    while True:
        try:
            stack = interpretLines(input(">>>"))
            executeStack(stack)
        except:
            exit(0)

if __name__ == "__main__":
    main()
