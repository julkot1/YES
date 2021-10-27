from abc import ABC, abstractmethod
import re
from enum import Enum
from shlex import shlex
from pyparsing import quotedString, removeQuotes



######################################################
# CONST
######################################################
DIGITS = '0123456789'

######################################################
#Split array function
######################################################
def splitArray(array, splitter):
    newArray = []
    subArray = []
    for ob in array:
        if ob == splitter:
            newArray.append(subArray.copy())
            subArray.clear()
        else:
            subArray.append(ob)
    if subArray != []:
        newArray.append(subArray)
    return newArray

######################################################
# Defaults statements
######################################################

class Tokens(Enum):
    PRINT = "ECHO"
    CREATE_VARIABLE = "_VAR"
    OPERATION = "OP"
    IF = "IF"
    SCOPE_BODY = "SC_BODY"
    MOVE = "MV"
    INPUT = "IN"
    GET_VAL_FROM_REF = "RV"
    INSTERT_TO_ARRAY = "INSERT"
    ITERATE = "IT"
    RETURN = "RT"
    def impl(self, statements):
        if self == self.PRINT:
            return Print().execute_init(statements)
        if self == self.RETURN:
            return Return().execute_init(statements)
        if self == self.SCOPE_BODY:
            return ScopeBody().execute_init(statements)
        if self == self.INPUT:
            return Input().execute_init(statements)
        if self == self.OPERATION:
            return Operation().execute_init(statements)
        if self == self.CREATE_VARIABLE:
            return CreateVar().execute_init(statements)
    @classmethod
    def has_value(cls, value):
        return value in cls._value2member_map_

######################################################
#Statement class
######################################################

class Statement:
    def __init__(self, expr):
        if isinstance(expr[0], list):
            self.isActive = True
            self.type = Tokens.SCOPE_BODY
        else:
            self.isActive = Tokens.has_value(expr[0])
        if self.isActive:
            self.statements = []
            if isinstance(expr[0], str):
                 self.type = Tokens(expr[0])
            newExpr = splitArray(expr, ';')
            for o in newExpr:
                for st in o[1::]:
                    self.statements.append(Statement(st))
        else:
            self.value = parseValue(expr)
    def __str__(self):
        if self.isActive:
            return self.type.value+" ("+'  '.join([str(elem) for elem in self.statements])+")"
        else:
            return self.value
    def toCode(self):
        return self.__toCode(self)
    def __toCode(self, statement):
        code = ''
        if statement.isActive:
            code += statement.type.value + " "
            for st in statement.statements:
                if st.isActive:
                    code += ' { '
                code += self.__toCode(st)
                if st.isActive:
                     code += ' } '
        else:
            code += ' '+statement.value
        return code
    def execute(self):
        if self.isActive:
            return self.type.impl(self.statements)
        else:
            return self.value

######################################################
#Variables
######################################################
class VariableRegister:
    _currentAddres = hex(0)
    _register = []
    def containsAddress(self, addres: int):
        for var in self._register:
            if var.addres == addres:
                return True
        return False
    def containsName(self, name: str):
        for var in self._register:
            if var.name == name:
                return True
        return False
    def getByName(self, name: str):
        for var in self._register:
            if var.name == name:
                if var.isReference:
                    return self.getByAddress(var.value)
                else:
                    return var
        return Null(NullishValues.UNDEFINED_VALUE)
    def getByAddress(self, address: int):
        for var in self._register:
            if var.address == address:
                return var
        return Null(NullishValues.UNDEFINED_VALUE)
    def add(self, var):
        if not self.containsName(var.name):
            var.address = self._currentAddres
            self._register.append(var)
            self._currentAddres = hex(int(self._currentAddres, 16)+1)
######################################################
# REGISTER
######################################################
def parseString(out):
    try:
        string = quotedString.setParseAction(None).parseString(out)
        if len(string) > 1:
            return Null(NullishValues.UNDEFINED_VALUE)
        elif string[0] == out:
            return quotedString.setParseAction(removeQuotes).parseString(out)[0]
    except Exception as inst:
        return Null(NullishValues.UNDEFINED_VALUE)
    return Null(NullishValues.UNDEFINED_VALUE)
def parseInt(out):
    if out[0] == '-' or out[0] in DIGITS:
        for char in out[1::]:
            if char in DIGITS:
                pass
            else:
                return Null(NullishValues.UNDEFINED_VALUE)
    else:
        return Null(NullishValues.UNDEFINED_VALUE)
    return int(out)
def parseFloat(out):
    hasFloatingPoint = False
    if out[0] == '-' or out[0] in DIGITS or out[0] == '.':
        for char in out[1::]:
            if char in DIGITS or char == '.':
                if char == '.':
                    if hasFloatingPoint:
                        return Null(NullishValues.UNDEFINED_VALUE)
                    else:
                        hasFloatingPoint=True
            else:
                return Null(NullishValues.UNDEFINED_VALUE)
    else:
        return Null(NullishValues.UNDEFINED_VALUE)
    return float(out)
def parseBoolean(out):
    if out == "true"  or out == "1":
        return Boolean.true
    elif out == "false" or out == "0":
        return Boolean.false
    return Null(NullishValues.UNDEFINED_VALUE)
def parseArray(out: str, type):
    if out.startswith('[') and out.endswith(']'):
        return out[1:-1:].split(',')
    return Null(NullishValues.UNDEFINED_VALUE)
def parseRef(out):
    if register.containsName(out):
        return register.getByName(out).address
    return Null(NullishValues.UNDEFINED_VALUE)
def parseNamespace(out: str):
    if re.match('[a-zA-Z][A-Za-z0-9_-]*$', out):
        return out
    return Null(NullishValues.UNDEFINED_VALUE)
def parseValue(out):
    VariableTypes.hasType(out)
    if Operation.Operators.getByToken(out):
        return out
    if VariableTypes.hasType(out):
        return out
    o = parseNamespace(out)
    if isinstance(o, Null):
        o = parseString(out)
    if isinstance(o, Null):
        o = parseInt(out)
    if isinstance(o, Null):
        o = parseFloat(out)
    if isinstance(o, Null):
        o = parseBoolean(out)
    if isinstance(o, Null):
        o = parseArray(out, None)
    if isinstance(o, Null):
        o = parseRef(out)
    return o
class VariableType:
    def __init__(self, token, valuesParser, canExtend = False):
        self.token = token
        self.canExtend = canExtend
        self.valuesParser = valuesParser
class VariableTypes(Enum):
    STRING = VariableType("String", parseString)
    INT = VariableType("Int", parseInt)
    FLOAT = VariableType("Float", parseFloat)
    BOOLEAN = VariableType("Bool", parseBoolean)
    ARRAY = VariableType("Arr", parseArray, True)
    REF = VariableType("REF", parseRef)
    @classmethod
    def hasType(self, type):
        for t in self._value2member_map_.values():
            if t.value.token == type:
                return True
        return False
class ValueTypes(Enum):
    STRING = VariableType("String", parseString)
    INT = VariableType("Int", parseInt)
    FLOAT = VariableType("Float", parseFloat)
    BOOLEAN = VariableType("Bool", parseBoolean)
    VAR_NAME = VariableType("VarName", parseString)
    OPERATOR = VariableType("Operator", parseString)
class Value:
    def __init__(self, value, type: VariableTypes):
        self.value = value
        self.type = type
    def parse(self, out):
        if self.type == VariableTypes.STRING:
            return parseString(out)
        elif self.type == VariableTypes.INT:
            return parseInt(out)
        elif self.type == VariableTypes.FLOAT:
            return parseFloat(out)
        elif self.type == VariableTypes.BOOLEAN:
            return parseBoolean(out)
class Variable(Value):
    def __init__(self, name, value, isReference, type):
        self.name = name
        self.isReference = isReference
        self.type = type
        self.value = self.parse(value)
        self.address = None
    def register(self, addres):
        self.address = addres
        return self
    def __eq__(self, o):
        return self.name == o
def isEmptyString(var: Variable):
    return var.type == VariableTypes.STRING and var.value == ''
def isZero(var: Variable):
    return (var.type == VariableTypes.FLOAT or var.type == VariableTypes.INT) and var.value == 0
def isEmptyArray(var: Variable):
    return (var.type == VariableTypes.ARRAY) and var.value == []
def isUndefined(var: Variable):
    return var.value == None
class NullishValue:
    def __init__(self, name, isNullish):
        self.name = name
        self.isNullish = isNullish
class NullishValues(Enum):
    EMPTY_STRING = NullishValue('EMPTY_STRING', isEmptyString),
    ZERO_VALUE= NullishValue('ZERO', isZero),
    EMPTY_ARRAY = NullishValue('EMPTY_ARRAY', isEmptyArray),
    UNDEFINED_VALUE = NullishValue('UNDEFINED', isUndefined)
class Null:
    _token = 'Null'
    def __init__(self, type):
        self.token = self._token+'::'+type.name
    def __str__(self) -> str:
        return self.token
register = VariableRegister()
class Boolean(Enum):
    true = True
    false = False
    def __str__(self):
        return self.name

######################################################
#Statements implementations
######################################################


class StatementImpl(ABC):
    @abstractmethod
    def execute(self, outs):
        pass
    def execute_init(self, statements):
        outs = []
        if isinstance(statements, list):
            for st in statements:
                outs.append(st.execute())
        return self.execute(outs)
class Print(StatementImpl):
    def execute(self, outs):
        toPrint=''
        if isinstance(outs, list):
                for o in outs:
                    toPrint = toPrint+str(o)
        print(toPrint)
        return toPrint
class Return(StatementImpl):
    def execute(self, outs):
        toReturn=''
        if isinstance(outs, list):
            toReturn=' '.join(str(elem) for elem in outs)
        return ['returnable', toReturn]
class ScopeBody(StatementImpl):
    def execute(self, outs):
        for out in outs:
            if out[0] == 'returnable':
                return out[1]
        return None
class Input(StatementImpl):
    def execute(self, outs):
        return input()
class OperatorType(Enum):
        MATH = "MATH"
        LOGIC = "LOGIC"
class Operator:
    def __init__(self, type, token, evalToken):
        self.type = type
        self.token = token
        self.evalToken = evalToken
    def __eq__(self, o: object) -> bool:
        return self.token == o
class Operation(StatementImpl):
    class Operators(Enum):
        ADD = Operator(OperatorType.MATH, "ADD" , '+')
        SUBSTACT = Operator(OperatorType.MATH, "SUB", '-')
        MULTIPLY = Operator(OperatorType.MATH, "MUL", '*')
        MODULO = Operator(OperatorType.MATH, "MOD", '%')
        DIVIDE = Operator(OperatorType.MATH, "DIV", '/')
        EQUAL = Operator(OperatorType.LOGIC, "EQ", '==')
        EQUAL_OR_GT = Operator(OperatorType.LOGIC, "EQ_GT", '>=')
        EQUAL_OR_LW = Operator(OperatorType.LOGIC, "EQ_LW", '<=')
        NOT = Operator(OperatorType.LOGIC, "NOT", '!=')
        GRATER_THAN = Operator(OperatorType.LOGIC, "GT", '>')
        LOWER_THAN = Operator(OperatorType.LOGIC, "LT", '<')
        AND = Operator(OperatorType.LOGIC, "OR", 'or')
        OR = Operator(OperatorType.LOGIC, "AND", 'and')
        @classmethod
        def getByToken(self, token) -> Operator:
            for it in self._member_map_.items():
                if it[1].value.token == token:
                    return it[1].value
            return None
    def execute(self, outs):
        toEval = ''
        for out in outs:
            op = self.Operators.getByToken(out)
            if op:
                toEval+=' '+op.evalToken+' '
            else:
                toEval+=str(out)
        return eval(toEval)
class CreateVar(StatementImpl):
    def execute(self, outs):
        print(len(outs))
        if(len(outs)==3):
            name = outs[0]
            type = outs[1]
            value = outs[2]
            print(name, type, value)
