#pragma once
#include <string>
#include "AstType.h"

namespace type
{
	std::string toCtoken(AstType* type);
	bool isStr(std::string token);
	bool isNumber(std::string token);
	bool isBool(std::string token);
	bool isLiteral(std::string token);
	AstType* getAstType(std::string token);
}