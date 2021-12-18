from pyparsing import nestedExpr, Combine, Regex
from enum import Enum
import re


REGISTERS_TOKENS = ['gr', 'xr', 'cr']
INNER_REGISTER = r'(?<=\[).+?(?=\])'
REGISTERS_REGEX = Combine(
    Regex(r'[a-z][a-z]')+'['+Regex(INNER_REGISTER)+']')


class StatementTokens(Enum):
    PUSH = "PUSH"
    ECHO = "ECHO"
    ADD = "ADD"
    SUB = "SUB"
    MUL = "MUL"
    DIV = "DIV"
    MOD = "MOD"


class SyntaxTokens(Enum):
    COMMENT = "//"
    IMPORT = "@import"
    ENDLINE = ";"
    NESTED_OPEN = "{"
    NESTED_END = "}"
    REGISTER_OPEN = "["
    REGISTER_END = "]"


def stripChar(string: str, char):
    return string.replace(char+' ', char).replace(' '+char, char)


def nest(token):
    return SyntaxTokens.NESTED_OPEN.value + token + SyntaxTokens.NESTED_END.value


def isNested(line):
    count = 0
    for char in line:
        if char == SyntaxTokens.NESTED_OPEN.value:
            count += 1
        elif char == SyntaxTokens.NESTED_END.value:
            count -= 1
    return count == 0


def joinNested(array):
    newArray = []
    stringBuffer = ''
    for element in array:
        if isinstance(element, list):
            newArray.append(stringBuffer)
            newArray.append(joinNested(element.copy()))
            stringBuffer = ''
        else:
            stringBuffer += element+' '
    if stringBuffer != '':
        newArray.append(stringBuffer)
    return newArray


def findRegisters(tokens):
    splitted = nestedExpr(SyntaxTokens.REGISTER_OPEN.value,
                          SyntaxTokens.REGISTER_END.value).parseString(SyntaxTokens.REGISTER_OPEN.value+tokens+SyntaxTokens.REGISTER_END.value).asList()[0]
    newTokens = []
    for index, token in enumerate(splitted):
        newToken = token
        if token in REGISTERS_TOKENS:
            if len(splitted) > index+1:
                if isinstance(splitted[index+1], list):
                    nested = joinNested(nestedExpr(SyntaxTokens.NESTED_OPEN.value,
                                                   SyntaxTokens.NESTED_END.value, ignoreExpr=REGISTERS_REGEX).parseString(nest(' '.join(a for a in splitted[index+1]))).asList()[0])
                    newToken = [newToken, getTokens(nested)[0]]
        if not isinstance(token, list):
            newTokens.append(newToken)

    return newTokens


def getTokens(array: list):
    tokens = []
    for element in array:
        if isinstance(element, str):
            statements = list(filter(lambda x: x != '', element.split(
                SyntaxTokens.ENDLINE.value)))
            for e in statements:
                e = findRegisters(e)
                if e != []:
                    if e[0] in [item.value for item in StatementTokens] or len(tokens) == 0:
                        tokens.append(e)
                    else:
                        tokens[len(tokens)-1].append(e)
        else:
            token = getTokens(element)
            if token != []:
                tokens[len(tokens)-1].append(token)

    return tokens


def getImports(lines):
    importLines = 0
    imports = []
    while lines[importLines].strip().startswith(SyntaxTokens.IMPORT.value):
        args = lines[importLines].split()
        assert args != 2, "Invalid import statement"
        imports.append(args[1])
        importLines += 1

    return (lines[importLines::], imports)


def lexFile(path):
    tokens = []
    with open(path) as inp:
        rows = ''
        lines, imports = getImports(inp.readlines())
        for line in lines:
            newRow = line.strip().split(SyntaxTokens.COMMENT.value)[0]
            newRow = stripChar(stripChar(
                newRow, SyntaxTokens.REGISTER_OPEN.value), SyntaxTokens.REGISTER_END.value)
            if newRow != '':
                rows += newRow
        nested = joinNested(nestedExpr(SyntaxTokens.NESTED_OPEN.value,
                                       SyntaxTokens.NESTED_END.value, ignoreExpr=REGISTERS_REGEX).parseString(nest(rows)).asList()[0])
        tokens = getTokens(nested)
        inp.close()
    return (imports, tokens)


class Statement:
    def __init__(self, token):
        self.token = token
        self.args = []

    def __str__(self):
        args = ' '
        if len(self.args) > 0:
            args = ' '.join(str(x) for x in self.args)
        return self.token + ': '+args


class Register():
    def __init__(self, token):
        self.token = token
        self.index = None


class Value():
    def __init__(self, token):
        self.token = token


def parseRegister(reg):
    if isinstance(reg, str):
        register = Register(reg)
        return register
    if len(reg) == 1:
        register = Register(reg[0])
        return register
    register = Register(reg[0])
    if reg[1][0] in [item.value for item in StatementTokens]:
        register.index = createStatement(reg[1])
    else:
        register.index = parseValue(reg[1][0])
    return register


def parseValue(val: str):
    value = Value(val)
    return value


def createStatement(token: list):
    statement = Statement(token[0])
    if len(token) > 1:
        for arg in token[1::]:
            if isinstance(arg, list):
                if arg[0] in REGISTERS_TOKENS:
                    statement.args.append(parseRegister(arg))
                else:
                    statement.args.append(createStatement(arg[0]))
            else:
                if arg in REGISTERS_TOKENS:
                    statement.args.append(parseRegister(arg))
                else:
                    statement.args.append(parseValue(arg))
    return statement


def createAST(tokens: list):
    stack = []
    for token in tokens:
        stack.append(createStatement(token))
    return stack


def printStatement(statement: Statement, space: int):
    print(" "*(space-2), statement.token+": ")
    for arg in statement.args:
        if isinstance(arg, Value):
            print(" "*space, arg.token)
        elif isinstance(arg, Statement):
            printStatement(arg, space+2)
        elif isinstance(arg, Register):
            print(" "*space, arg.token)
            if isinstance(arg.index, Value):
                print(" "*(space-1), arg.index.token)
            if isinstance(arg.index, Statement):
                printStatement(arg.index, space+1)


def printAST(ast: list):
    for statement in ast:
        printStatement(statement, 1)
