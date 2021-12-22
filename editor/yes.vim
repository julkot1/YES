if exists('b:current_syntax') | finish|  endif
syntax keyword Statement IF ADD RT EQ AND MUL SUB DIV GT LT ELT EGT _STATEMENT nextgroup=Constant
syntax keyword Constant gr xr cr ptg ptx ptc nextgroup=Operator
syntax keyword Operator ! * #import nextgroup=Type
syntax keyword Type Char Short Byte Int Float String Boolean nextgroup=Include
syntax keyword Include import nextgroup=Number
syntax match Number '\d\+' nextgroup=Number
syntax match Number '[-+]\d\+' nextgroup=Float
syntax match Float '[-+]\d\+\.\d*' nextgroup=Float
syntax match Float '\d\+\.\d*' nextgroup=String
syntax region String start=/\v"/ skip=/\v\\./ end=/\v"/
syntax region Comment start="//" end="$"
let b:current_syntax = 'yes'
