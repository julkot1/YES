#include "Keywords.h"


const std::string Keywords::BOOLEAN_FALSE = "true";
const std::string Keywords::BOOLEAN_TRUE = "false";
const std::string Keywords::SHORT = "Short";
const std::string Keywords::CHAR="Char";
const std::string Keywords::INT="Int";
const std::string Keywords::STR="Str";
const std::string Keywords::LONG="Long";
const std::string Keywords::FLOAT="Float";
const std::string Keywords::BOOLEAN="Bool";
const std::string Keywords::DOUBLE="Double";
const std::string Keywords::FUNCTION="Fun";
const std::string Keywords::CONST = "Const";
const std::string Keywords::PTR = "Ptr";
const std::string Keywords::DEF = "Def";
const std::string Keywords::C_FUNCTION = "CFun";
const std::string Keywords::NUM = "Num";
bool Keywords::isLiteral(std::string token)
{
	return token.compare(BOOLEAN_FALSE) == 0 || token.compare(BOOLEAN_TRUE) == 0;
}
bool Keywords::isType(std::string token){
	return token.compare(SHORT) == 0 
		|| token.compare(CHAR) == 0
		|| token.compare(INT) == 0
		|| token.compare(LONG) == 0
		|| token.compare(DOUBLE) == 0
		|| token.compare(FLOAT) == 0
		|| token.compare(STR) == 0
		|| token.compare(PTR) == 0
		|| token.compare(CONST) == 0
		|| token.compare(DEF) == 0
		|| token.compare(BOOLEAN) == 0
		|| token.compare(NUM) == 0
		|| token.compare(FUNCTION) == 0
		|| token.compare(C_FUNCTION) == 0;
}