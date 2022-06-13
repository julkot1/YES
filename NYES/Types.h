#pragma once
#include <string>
#include "AstType.h"

namespace type
{
	std::string toCtoken(AstType* type);
	bool isStr(std::string token);
}