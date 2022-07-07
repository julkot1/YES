# YES
YES is a simple programming language written in C++ without LLVM. The YES compiler (yesc) translates the code into C code. To build a working application, it is necessary to use the GCC compiler. It is an imperative, functional language. YES supports such features as:
- objects
- higher-order functions
- first-class functions 
- low-level memory management (pointers, memory allocation, etc.) 
- generic types
YES only runs on Linux distributions and uses glibc as a dependency!
## YES syntax
function invokation
```
FUNCTION_NAME ARG_1 ARG_2 ARG_n;
```
type assigment
```
(TYPE) IDENTIFIER/LITERAL
```
interface
```
[(TYPE_1) IDENTUFIER_1 (TYPE_N) IDENTUFIER_N]
```
function
```
{FUN_1 args; FUN_n args;};
```
