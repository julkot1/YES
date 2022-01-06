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
To compile YES program use `yes.py`. This program generates two files - `out.c` with generated C code and runnable `app`
```
python3 yes.py [filename] [-r flag to run]
```
To test YES programs use `test/test.py`. In `in` directory put your YES files. In `out` add files with exactly the same name as the files in the `in` directory with expected output. 
```
python3 tests.py
```

## Table of Contents
- [YES](#yes)
  - [Table of Contents](#table-of-contents)
  - [Data types](#data-types)
    - [Integer Types](#integer-types)
    - [Floating-Point Types](#floating-point-types)
    - [Other Types](#other-types)
  - [Arithmetic and logic operators](#arithmetic-and-logic-operators)
    - [Arithmetic operators](#arithmetic-operators)
    - [Bitwise operators](#bitwise-operators)
    - [Logic operators](#logic-operators)
  - [Default Statements](#default-statements)
    - [PUSH](#push)
    - [REPLACE](#replace)
    - [SET](#set)
    - [SWAP](#swap)
    - [gDEL & xDEL](#gdel--xdel)
    - [ECHO](#echo)
    - [IN](#in)
    - [DO](#do)
    - [RT](#rt)
    - [IF](#if)
    - [CALL](#call)
    - [REPEAT](#repeat)
    - [YELL](#yell)
    - [WHILE & doWHILE](#while--dowhile)
  - [Prefix](#prefix)
    - [call parent cr - `$`](#call-parent-cr---)
    - [reference to array - `&`](#reference-to-array---)

## Data types

### Integer Types
| Type | Values | Size|Format specifier|
| ------------- | ------------- | ------------- |------------- |
| Char  | 0 to 255  |1 byte| `%c` |
| Short  | -2<sup>15</sup> to 2<sup>15</sup>-1  | 2 bytes| `%s`|
| Int  | -2<sup>31</sup> to 2<sup>31</sup>-1 | 4 bytes|  `%i`|
| Long  | -2<sup>63</sup> to 2<sup>63</sup>-1  | 8 bytes| `%l`|
| Size | 0 to 2<sup>31</sup>-1  | 4 bytes| `%p`|

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
### PUSH 
puts value to `gr`
```
PUSH 5;
```
### REPLACE
replace `gr[0]` by value (must be the same type)
```
PUSH 5;
REPLACE 14;
ECHO "%i" gr;
```
out:
```
14
```

### SET 
set `pr` with index to new value 
```
$DO 5 {pSET 0 61}
  {
    ECHO "i%" pr
  };
```
out:
```
61
```
### SWAP
pass 2 arguments (must be array with the same type) and swap it.
```
PUSH 6;
PUSH 4;
ECHO "%c %c\n" (Char) gr (Char) gr[1];
SWAP (Char) &gr (Char) &gr[1];
ECHO "%c %c\n" (Char) gr (Char) gr[1];
```
out:
```
4 6
6 4
```
### gDEL & xDEL
delate n-elements form `gr` or `xr`.
```
PUSH 5;
PUSH 3;
PUSH 4;
ECHO "%i\n" gr;
gDEL 2;
ECHO "%i" gr;
```
out:
```
4
5
```
### ECHO
Is like printf in C
```
ECHO "%i\n" 6585; 
```
out:
```
6585
```
### IN
requires no arguments but type of statement is mandatory. Takes input from the console and puts it to the `xr`.
```
IN (Char);
ECHO "%c" (Char) xr;
```
for in:
```
2
```
out:
```
2
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
out:
```
2
```
### CALL
is like system in C. Takes 1 or many arguments (like ECHO) and execute it as shell command.

```
CALL "mkdir %S" "dir";
CALL "ls";
```
out:
```
dir
```
### REPEAT
passes 2 arguments. The first one is a value of integer type. The program will execute the second argument as many times as it was given in the first argument.
```
REPEAT 5 {
  ECHO "giggity\n"
}
```
out:
```
giggity
giggity
giggity
giggity
giggity
```
### YELL
requires 2 (`Bool` and `Str`) arguments. If the first argument is false, the program prints the second argument to the console and exits.

### WHILE & doWHILE
The behavior of these statements is the same as in C:
- WHILE will execute the second argument until the first argument is true.
```
PUSH (Int) 5;
$WHILE {GT (Int) gr 0} {
    ECHO "%i\n" (Int) gr;
    REPLACE {SUB (Int) gr 1};
    SET 0 {GT (Int) gr 0};
};
```
out:
```
5
4
3
2
1
```
- doWHILE must do second argument at least one independently of the value of the condition.
```
PUSH (Int) 0;
$doWHILE {GT (Int) gr 0} {
    ECHO "%i\n" (Int) gr;
    REPLACE {SUB (Int) gr 1};
    SET 0 {GT (Int) gr 0};
};
```
out:
```
0
```


## Prefix

### call parent cr - `$`
To get access children statement to the parent `cr` as `pr` insert the prefix `$` into the parent.
```
$DO "giggity" {
  ECHO "%S" (Str) pr;
}
```
out:
```
giggity
```
DO is parent for arguments "giggity" and nested statement. So they are its children.
but

```
$DO "giggity" {
  DO 5 {
    ECHO "%S" (Str) pr;
  }
}
```
out:
```
giggity
```
It will display "giggity" instead of 5 because `$` was called in first `DO` statement.


### reference to array - `&`
To refer an array element to another use this prefix.
```
PUSH 5;
$DO (Char) &gr {
  SET 0 10;
}
ECHO "%c" (Char) gr; 
```
out:
```
10
```