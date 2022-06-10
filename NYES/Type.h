#pragma once
#include "ast.h"

class Type
{
private:
	ast::PrimitiveTypes type;
	bool isComplex;
	std::vector<ast::PrimitiveTypes> children;
public:
	Type(ast::PrimitiveTypes type);
	ast::PrimitiveTypes get();
	ast::PrimitiveTypes nextChild();
};

