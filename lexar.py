from pyparsing import nestedExpr, Combine, Regex
from enum import Enum
import re


REGISTERS_TOKENS = ['gr', 'xr']
INNER_REGISTER = r'(?<=\[).+?(?=\])'
REGISTERS_REGEX = Combine(
    Regex(r'[a-z][a-z]')+'['+Regex(INNER_REGISTER)+']')


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
                    tokens.append(e)
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


print(lexFile("program.yes"))
