<div align="center">
  <a href="https://github.com/julkot1/YES">
    <img src="https://github.com/julkot1/YES/blob/main/logo.svg" alt="Logo" width="250" height="187.5">
  </a>
</div>
</br>


# YES
YES an esoteric programming language written in [Python](https://www.python.org/).
Yes is a structured, register-based, strong and static typed programming language. 
</br>
Every statment has n statments or values like `ECHO "SUM 2+2 is " {ADD 2 2}` this statment prints `SUM 2+2 is 4`.


## Sytax
Basic YES schema:
```
STATEMANT VALUE|{ANOTHER_STATEMENT  VALUE|YET_ANOTHER_STATEMENT ...} VALUE|{ANOTHER_STATEMENT ...} ...
```

## Data types

### Integer Types
| Type | Values | Size|
| ------------- | ------------- | ------------- |
| Char  | 0 to 255  |1 byte|
| Short  | -2<sup>15</sup> to 2<sup>15</sup>-1  | 2 bytes|
| Int  | -2<sup>31</sup> to 2<sup>31</sup>-1 | 4 bytes|
| long  | -2<sup>63</sup> to 2<sup>63</sup>-1  | 8 bytes|

### Floating-Point Types
| Type | Values | Precision | Size|
| ------------- | ------------- | ------------- |------------- |
| Float  | 1.2E-38 to 3.4E+38  |6 decimal places |4 byte|
| Double  | 2.3E-308 to 1.7E+308| 15 decimal places |8 byte|

### Other Types
| Type | Values | Size|
| ------------- | ------------- | ------------- |
| Boolean  | `true` or `false`  | 1 byte|
| Str  | `"Text"`  | - |

## Arithmetic and logic opertors

### Arithmetic operators

| Operator | Meaning |
| ------------- | ------------- |
| ADD  | `+`  |
| SUB | `-`  |
| MUL  | `*`  |
| DIV  | `/`  |
| MOD  | `%`  |




