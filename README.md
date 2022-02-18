<div align="center">
  <a href="https://github.com/julkot1/YES">
    <img src="https://github.com/julkot1/YES/blob/main/logo.svg" alt="Logo" width="250" height="187.5">
  </a>
</div>
</br>

# YES

YES an esoteric "programming language" written in [Java](https://www.java.com/).
Yes is a structured, array-based, strong typed "programming language".
</br>
To compile YES program use `yes-compiler`. This program generates two files - `out.c` with generated C code and runnable `app`

```
./run.sh [filename]
```
Flags:

* -r  run app
* -o [name] out file name (default app)
* -j create compiler from `java` source code


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
  - [Arrays](#arrays)
    - [gr](#gr)
    - [xr](#xr)
  - [Statement](#statement)
  - [Scope](#scope)
  - [Default Statements](#default-statements)
    - [PUSH](#push)
    - [REPLACE](#replace)
    - [ALLOC](#alloc)
    - [DEL](#del)
    - [IN](#in)
    - [RT](#rt)
    - [IF](#if)
    - [CALL](#call)
    - [SYSCALL](#syscall)
    - [REPEAT](#repeat)
    - [YELL](#yell)
    - [atCHAR](#atchar)
    - [LEN](#len)
    - [FORMAT](#format)
    - [MV](#mv)
  - [Statement declaration](#statement-declaration)
    - [syntax schema:](#syntax-schema)
    - [_INTERFACE](#_interface)
      - [ARG](#arg)
      - [syntax schema](#syntax-schema-1)
    - [example](#example)
  - [Prefix](#prefix)
    - [reference to array - `&`](#reference-to-array---)
 
## Data types
 
### Integer Types
 
| Type  | Values                              | Size    | Format specifier |
| ----- | ----------------------------------- | ------- | ---------------- |
| Char  | 0 to 255                            | 1 byte  | `%c`             |
| Short | -2<sup>15</sup> to 2<sup>15</sup>-1 | 2 bytes | `%s`             |
| Int   | -2<sup>31</sup> to 2<sup>31</sup>-1 | 4 bytes | `%i`             |
| Long  | -2<sup>63</sup> to 2<sup>63</sup>-1 | 8 bytes | `%l`             |
| Size  | 0 to 2<sup>31</sup>-1               | 4 bytes | `%p`             |
 
### Floating-Point Types
 
| Type  | Values             | Precision        | Size    | Format specifier |
| ----- | ------------------ | ---------------- | ------- | ---------------- |
| Float | 1.2E-38 to 3.4E+38 | 6 decimal places | 4 bytes | `%f`             |
 
### Other Types
 
| Type    | Values                   | Size   | Description   | Format specifier |
| ------- | ------------------------ | ------ | ------------- | ---------------- |
| Boolean | `true` or `false`        | 1 byte | logic value   | `%b`             |
| Str     | `"Text"`                 | -      | text          | `%S`             |
| Void    | `2u`                 | -      | empty type          | `%v`             |
 
## Arithmetic and logic operators
 
### Arithmetic operators
 
| Operator | Operator in C |
| -------- | ------------- |
| ADD      | `+`           |
| SUB      | `-`           |
| MUL      | `*`           |
| DIV      | `/`           |
| MOD      | `%`           |
 
### Bitwise operators
 
| Operator | Operator in C |
| -------- | ------------- |
| bAND     | `&`           |
| bOR      | `\|`          |
| bNOT     | `~`           |
| XOR      | `^`           |
| LSHIFT   | `<<`          |
| RSHIFT   | `>>`          |
 
### Logic operators
 
| Operator | Operator in C |
| -------- | ------------- |
| AND      | `&&`          |
| OR       | `\|\|`        |
| NOT      | `!`           |
| EQ       | `==`          |
| nEQ      | `!=`          |
| LT       | `<`           |
| GT       | `>`           |
| eLT      | `<=`          |
| eGT      | `>=`          |
 
## Arrays
 
### gr
 
is general purpose, available in all scope array. `gr` can store any type of data. Every new element added to array is always on top with index 0. Index of `gr` could be a number or array of `Size` type. Size of `gr` is 30000.
<br/>
`gr` statements: `PUSH`, `SWAP`, `DEL`, `ALLOC`, `REPLACE`.
<br/>
 
```
PUSH 6;                 PUSH 5;                     DEL 1;
| gr | value |          | gr | value |              | gr | value |
| 0  | 6     |    -->   | 0  | 5     |      -->     | 0  | 6     |
                        | 1  | 6     |
```
### xr
 
is available in all scope, contains special functions with 16 size array. `xr` could store only 32 bytes data type. Index of `xr` must be a constance number.
 
| Array element | Function |
| -------- | ------------- |
| `xr[0]`|  statement return value|
| `xr[1]`|  loops special functions|
| `xr[2]`|  read output|
| `xr[3...15]`| no special functions|
 
## Statement
 
Statement invoke:
```
(T) STATEMENT arg_0 ... arg_n;
```
* T - is type of STATEMENT
* STATEMENT - is name of statement
* arg_0 ... arg_n - are arguments
 
Statement return value is set to `xr[0]` if statement is on top of a scope (is not argument).
 
```
ADD 1 3; # --> xr[0] is 4
 
```
 
## Scope
* `GLOBAL` - is on top of the file
* `NS` - is in nested statement
* `SD` - is in `_STATEMENT` body
 
Single nested statement is not a scope! 
 
program.yes
```
_STATEMENT FOO {
    RT 2; # SD scope
};
SD; # GLOBAL scope
ADD 1 {
    ADD 3 2; # NS scope
    RT {ADD (Char) xr[0] 1};
};
```
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
 
### ALLOC
 
allocates n cells in memory as element in `gr` array. If `ALLOC` is type of `Str` then `Str` with size n is allocated.
 
```
ALLOC 10;
ECHO "%v" (Void) gr;
```
 
out:
 
```
0u
```
 
### DEL
 
delate n-elements form `gr`.
 
```
PUSH 5;
PUSH 3;
PUSH 4;
ECHO "%i\n" gr;
DEL 2;
ECHO "%i" gr;
```
 
out:
 
```
4
5
```
 
### IN
 
requires no arguments but type of statement is mandatory. Takes input from the console and puts it to the `xr[2]`.
 
```
IN (Char);
ECHO "%c" (Char) xr[2];
```
 
for in:
 
```
2
```
 
out:
 
```
2
```
 
### RT
 
Is returns statement, pass one argument and puts its into `xr[0]`. It could be used only in nested statements.
 
```
_STATEMENT FOO (Char) {
  RT 5;
};
```
 
### IF
 
passes 1 or 2 arguments. Statement will return and execute first argument if condition (`xr[0]`) is true or second (witch is optional) if it false. If type is specified then `IF` statement will return 
 
```
ECHO "%i" {
  (Int) IF {EQ 2 2 (Bool)} 2 3 
};
```
 
out:
 
```
2
```
 
### CALL
 
is like system in C. Takes 1 argument and execute it as shell command.
 
```
CALL "mkdir dir";
CALL "ls";
```
 
out:
 
```
dir
```
 
### SYSCALL
 
requires min 1 argument of `Int` type. `SYSCALL` statement that calls a system call whose assembly language interface has a number as the first argument.
<br>
[Table of linux syscalls](https://filippo.io/linux-syscall-table/)
 
```
SYSCALL 1 0 "Hello" 5;
```
 
out:
 
```
Hello
```
 
### REPEAT
 
passes 2 arguments. The first one is a value of integer type. The program will execute the second argument as many times as it was given in the first argument. Statement stores iteration count in `xr[1]`.
 
```
REPEAT 5 {
  ECHO "%i\n" (Int) xr[1];
}
```
 
out:
 
```
0
1
2
3
4
```
 
### YELL
 
requires 2 (`Bool` and `Str`) arguments. If the first argument is false, the program prints the second argument to the console and exits.

### atCHAR

requires 2 (`Str` and `Size`) arguments and returns `Char` at index (2nd argument) of string (1st argument).

### LEN

requires 1 `Str` argument and gives length of this string.

### FORMAT

requires min 2 (first `Str` template) arguments. It returns concatenated Str.

### MV

requires 2 arguments with the same type. First one must be the `xr[n]`. This statement push second arg to `xr[n]`
## Statement declaration
To define new statement use `_STATEMENT`. It can be used only in `GLOBAL` scope.
Name contains only letters, numbers and `_`.
<br/>

### syntax schema:

```
_STATEMENT NAME {};
```
****

### _INTERFACE

Each Statement might have an interface. An interface defines types and quantity of arguments. Definition of interface must be placed before the matching statement declaration. `_INTERFACE` requires at least 2 arguments. First one is name (like in `_STATEMENT`). The next arguments are single nested statements `{ARG}`.

#### ARG

requires 0 or 1 digit argument. Type of statement must be specified. This statement defines how much arguments of type is in `_STATEMENT`. No argument in `ARG` means 1 argument of given type.

#### syntax schema

```
_INTERFACE NAME {(T) ARG n} ...;
```

### example

```
_INTERFACE FOO {(Int) ARG 2} {(Str) ARG};
_STATEMENT FOO {...};
FOO (Int) 4 (Int) 43 "bar";

```

## Prefix
### reference to array - `&`

To refer an array element to another use this prefix.