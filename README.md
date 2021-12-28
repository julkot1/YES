<div align="center">
  <a href="https://github.com/julkot1/YES">
    <img src="https://github.com/julkot1/YES/blob/main/logo.svg" alt="Logo" width="250" height="187.5">
  </a>
</div>
</br>


# YES
YES an esoteric "programming language" written in [Python](https://www.python.org/).
Yes is a structured, array-based, strong typed "programming language". 
</br>
Every statment has n statments or values like `ECHO "SUM 2+2 is " {ADD 2 2}` this statment prints `SUM 2+2 is 4`.


## Sytax
Basic YES schema:
```
STATEMANT VALUE|{ANOTHER_STATEMENT  VALUE|YET_ANOTHER_STATEMENT ...} VALUE|{ANOTHER_STATEMENT ...} ...
```

## Data types

### Integer Types
| Type | Values | Size|Format specifier|
| ------------- | ------------- | ------------- |------------- |
| Char  | 0 to 255  |1 byte| `%c` |
| Short  | -2<sup>15</sup> to 2<sup>15</sup>-1  | 2 bytes| `%s`|
| Int  | -2<sup>31</sup> to 2<sup>31</sup>-1 | 4 bytes|  `%i`|
| long  | -2<sup>63</sup> to 2<sup>63</sup>-1  | 8 bytes| `%l`|

### Floating-Point Types
| Type | Values | Precision | Size|Format specifier|
| ------------- | ------------- | ------------- |------------- |------------- |
| Float  | 1.2E-38 to 3.4E+38  |6 decimal places |4 bytes| `%f`|

### Other Types
| Type | Values | Size| Description|Format specifier|
| ------------- | ------------- | ------------- |-------------|-------------|
| Boolean  | `true` or `false`  | 1 byte|logic value| `%b`|
| Type  | `Int`, `Str`, `Char` ...  | 1 byte|type of value| `%t`|
| Str  | `"Text"`  | - | text| `%S`|


## Arithmetic and logic operators

### Arithmetic operators

| Operator | Operator in C |
| ------------- | ------------- |
| ADD  | `+`  |
| SUB | `-`  |
| MUL  | `*`  |
| DIV  | `/`  |
| MOD  | `%`  |


### Bitwise operators

| Operator | Operator in C |
| ------------- | ------------- |
| bAND  | `&`  |
| bOR| `\|`  |
| bNOT  | `~`  |
| XOR | `^`  |
| LSHIFT  | `<<`  |
| RSHIFT  | `>>`  |

### Logic operators

| Operator | Operator in C |
| ------------- | ------------- |
| AND  | `&&`  |
| OR| `\|\|`  |
| NOT  | `!`  |
| EQ| `==`  |
| nEQ| `!=`  |
| LT| `<`  |
| GT| `>`  |
| eLT| `<=`  |
| eGT| `>=`  |



## Default Statements

### ECHO
Is like printf in c
```
ECHO "%i\n" 6585; 
```
out:
```
6585
```
### DO
Passes n arguments of any type
```
DO gr 6 {ADD 1 2}; 
```
### RT
Is returns statement, pass n arguments and puts them into xr. It could be used only in nested statements.
```
DO {
  RT 5;
}; 
```
### IF 
passes 2 or 3 arguments. First one is logic condition. Statement will return and execute second arg if condition is true or third (witch is optional) if it false.
```
ECHO "%i" {
  IF {EQ 2 2 (Bool)} 2 3 (Int)
};
```
out
```
2
```