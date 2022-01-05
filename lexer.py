from pyparsing import nestedExpr, Combine, Regex
from enum import Enum

REGISTERS_TOKENS = ['gr', 'xr', 'cr', 'pr']
BOOLEAN_TOKENS = ['true', 'false']
INNER_REGISTER = r'(?<=\[).+?(?=\])'
REGISTERS_REGEX = Combine(
    Regex(r'[^ ][a-z]r')+'['+Regex(INNER_REGISTER)+']')


class PrefixTokens(Enum):
    CALL_PR = "$"
    REFERENCE = "&"


class StatementTokens(Enum):
    PUSH = "PUSH"
    ECHO = "ECHO"
    ADD = "ADD"
    SUB = "SUB"
    MUL = "MUL"
    DIV = "DIV"
    MOD = "MOD"
    BNOT = "bNOT"
    BOR = "bOR"
    BAND = "bAND"
    XOR = "XOR"
    LSHIFT = "LSHIFT"
    RSHIFT = "RSHIFT"
    EQ = "EQ"
    LT = "LT"
    GT = "GT"
    ELT = "eLT"
    EGT = "eGT"
    NOT = "NOT"
    NEQ = "nEQ"
    OR = "OR"
    AND = "AND"
    DO = "DO"
    RT = "RT"
    IF = "IF"
    CALL = "CALL"
    IN = "IN"
    WHILE = "WHILE"
    DOWHILE = "doWHILE"
    REPEAT = "REPEAT"
    YELL = "YELL"
    GDEL = "gDEL"
    XDEL = "xDEL"
    REPLACE = "REPLACE"
    SET = "SET"


class SyntaxTokens(Enum):
    COMMENT = "//"
    IMPORT = "@import"
    ENDLINE = ";"
    STRING = '"'
    NESTED_OPEN = "{"
    NESTED_END = "}"
    REGISTER_OPEN = "["
    REGISTER_END = "]"
    CAST_OPEN = "("
    CAST_END = ")"
    NULL = "null"


def getPrefix(token: str):
    prefix = []
    shift = 0
    for char in token:
        if char in [item.value for item in PrefixTokens]:
            prefix.append(char)
            shift += 1
        else:
            break
    return (prefix, token[shift::])


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
        if getPrefix(token)[1] in REGISTERS_TOKENS:
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
                    p, t = getPrefix(e[0])
                    if t in [item.value for item in StatementTokens] or len(tokens) == 0:
                        tokens.append(e)
                    else:
                        for arg in e:
                            tokens[len(tokens)-1].append(arg)
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


class Argument:
    def __init__(self):
        self.token = ''
        self.prefix = []
        self.type = None


class Statement(Argument):
    def __init__(self, token):
        self.token = token
        self.args = []

    def __str__(self):
        args = ' '
        if len(self.args) > 0:
            args = ' '.join(str(x) for x in self.args)
        return self.token + ': '+args


class Register(Argument):
    def __init__(self, token, ytype):
        self.token = token
        self.index = None
        self.type = ytype


class Value(Argument):
    def __init__(self, token):
        self.token = token


def parseRegister(reg, ytype):
    if isinstance(reg, str):
        p, t = getPrefix(reg)
        register = Register(t, ytype)
        register.prefix = p
        return register
    if len(reg) == 1:
        p, t = getPrefix(reg[0])
        register = Register(t, ytype)
        register.prefix = p
        return register
    p, t = getPrefix(reg[0])
    register = Register(t, ytype)
    register.prefix = p
    if reg[1][0] in [item.value for item in StatementTokens]:
        register.index = createStatement(reg[1], None)
    else:
        register.index = parseValue(reg[1][0], None)
    return register


def parseValue(val: str, ytype):
    value = Value(val)
    value.type = ytype
    return value


def createStatement(token: list, typeCast):
    p, t = getPrefix(token[0])
    statement = Statement(t)
    statement.prefix = p
    if len(token) > 1:
        for arg in token[1::]:
            if isinstance(arg, list):
                p, t = getPrefix(arg[0])
                if t in REGISTERS_TOKENS:
                    statement.args.append(parseRegister(arg, typeCast))
                    typeCast = None
                else:
                    if len(arg) == 1:
                        statement.args.append(
                            createStatement(arg[0], typeCast))
                    else:
                        statements = list(createStatement(a, None)
                                          for a in arg)
                        statement.args.append(statements)
                    typeCast = None
            else:
                p, t = getPrefix(arg)
                if arg.startswith(SyntaxTokens.CAST_OPEN.value) and arg.endswith(SyntaxTokens.CAST_END.value):
                    typeCast = arg[1:len(arg)-1:]
                elif t in REGISTERS_TOKENS:
                    statement.args.append(parseRegister(arg, typeCast))
                    typeCast = None
                else:
                    statement.args.append(parseValue(arg, typeCast))
                    typeCast = None
        statement.type = typeCast
        typeCast = None
    return statement


def createAST(tokens: list):
    stack = []
    for token in tokens:
        stack.append(createStatement(token, None))
    return stack


def printStatement(statement: Statement, space: int, types):
    print(" "*(space-2), statement.token+": ", types)
    for arg in statement.args:
        if isinstance(arg, Value):
            print(" "*space, arg.token, arg.type)
        elif isinstance(arg, Statement):
            printStatement(arg, space+2, arg.type)
        elif isinstance(arg, Register):
            print(" "*space, arg.token, arg.type)
            if isinstance(arg.index, Value):
                print(" "*(space-1), arg.index.token, arg.type)
            if isinstance(arg.index, Statement):
                printStatement(arg.index, space+1, arg.type)


def printAST(ast: list):
    for statement in ast:
        printStatement(statement, 1, statement.type)
