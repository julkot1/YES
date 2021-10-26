<div align="center">
  <a href="https://github.com/julkot1/YES">
    <img src="https://github.com/julkot1/YES/blob/main/logo.svg" alt="Logo" width="250" height="187.5">
  </a>
</div>
</br>


# YES
YES an esoteric programming language written in [Python](https://www.python.org/).
YES is a recursive acronym from: YES - Yes Extendable Statements. Yes is a structured, strong and static typed programming language. 
</br>
Every statment has n statments or values like `ECHO "SUM 2+2 is " {OP 2 ADD 2}` this statment prints `SUM 2+2 is 4`. Also each statement returns value e.g `ECHO {ECHO "foo"}` - prints `foofoo` because `ECHO "foo"` display data in termial and also returns this data. 
</br>
To interpret your code use [YESman]() interpreter.
## Sytax
Basic YES schema:
```
STATEMANT VALUE|{ANOTHER_STATEMENT  VALUE|YET_ANOTHER_STATEMENT ...} VALUE|{ANOTHER_STATEMENT ...} ...
```
### Data types
| Type | Values | Example|
| ------------- | ------------- | ------------- |
| String  | Text  |`"text"` or `'tes"dsd"t'`|
| Int  | Inteagers  |`5` or `-638`|
| Float  | Calculable numbers  |`5.25` or `.8`|
| Boolean  | Logic  |`true` or `false`|
| Arr  | Array  |`[1,23,4,5]` or `["foo", "bar"]`|
| REF  | Address of the variable  |`0x123` or `0x5a`|

### Arithmetic and logic opertors

To eval expression use statement `OP VALUE OPERATOR VALUE ...`

#### Arithmetic operators

| Operator | Meaning |
| ------------- | ------------- |
| ADD  | `+`  |
| SUB | `-`  |
| MUL  | `*`  |
| DIV  | `/`  |
| MOD  | `%`  |

#### Logic operators

| Operator | Meaning (c++ like)|
| ------------- | ------------- |
| GT  | `>`  |
| LT  | `<`  |
| EQ  | `==`  |
| NOT | `!`  |
| EQ_GT  | `=>`  |
| EQ_LT  | `=<`  |
| AND  | `&&`  |
| OR  | `or`|

### Basic statements

### ECHO
Displays concatenated values of any type.
```
ECHO VALUE_1 VALUE_2 ...
```
Example:
```
ECHO "foo" 2 true
```
Prints `foo2true` and returns `foo2true`.
### IN
Get input from console and prints like `ECHO` but returns user's input `String`.
```
IN VALUE_1 VALUE_2 ...
```
Example:
```
IN "Type name: "
```
Prints `Type name` and returns user's input.

### OP
Eval operation and returns result `Int` or `Float` or `Boolean`
```
OP VALUE OPERATOR VALUE ...
```
Example:
```
OP 2 MUL {OP 1 ADD 3}
```
Resolve `2*(1+3)` and retuns result `8`

