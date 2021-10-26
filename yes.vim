if exists('b:current_syntax') | finish|  endif
syntax keyword Statement ECHO OP IF SC_BODY IN MV RT INSERT RV TYPE CAST IT _VAR _STATEMENT nextgroup=Operator
syntax keyword Operator ADD MUL DIV SUB MOD GT EQ NOT EQ_GT EQ_LT LT nextgroup=Tag
syntax keyword Tag Int Float String Boolean Array nextgroup=Number
syntax match Number '\d\+' nextgroup=Number
syntax match Number '[-+]\d\+' nextgroup=Float
syntax match Float '[-+]\d\+\.\d*' nextgroup=Float
syntax match Float '\d\+\.\d*' nextgroup=String
syntax region String start=/\v"/ skip=/\v\\./ end=/\v"/
let b:current_syntax = 'yes'
