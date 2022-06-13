#include "ast.h"

namespace ast
{
	bool isComplexType(PrimitiveTypes type)
	{
		return type == PrimitiveTypes::DEF
			|| type == PrimitiveTypes::CONST
			|| type == PrimitiveTypes::FUN
			|| type == PrimitiveTypes::CFUN
			|| type == PrimitiveTypes::PTR;
	}
	bool isPrimitiveType(PrimitiveTypes type)
	{
		return !isComplexType(type);
	}
	PrimitiveTypes getType(std::string str)
	{
		if (Keywords::SHORT.compare(str) == 0) return SHORT;
		else if (Keywords::CHAR.compare(str) == 0) return CHAR;
		else if (Keywords::INT.compare(str) == 0) return INT;
		else if (Keywords::STR.compare(str) == 0) return STR;
		else if (Keywords::LONG.compare(str) == 0) return LONG;
		else if (Keywords::FLOAT.compare(str) == 0) return FLOAT;
		else if (Keywords::BOOLEAN.compare(str) == 0) return BOOLEAN;
		else if (Keywords::DOUBLE.compare(str) == 0) return DOUBLE;
		else if (Keywords::FUNCTION.compare(str) == 0) return FUN;
		else if (Keywords::CONST.compare(str) == 0) return CONST;
		else if (Keywords::NUM.compare(str) == 0) return NUM;
		else if (Keywords::PTR.compare(str) == 0) return PTR;
		else if (Keywords::DEF.compare(str) == 0) return DEF;
		else if (Keywords::C_FUNCTION.compare(str) == 0) return CFUN;
		else throw;
		return SHORT;
	}
};