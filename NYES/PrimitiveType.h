#pragma once
#include "ast.h"
class PrimitiveType
{
private:
	ast::PrimitiveTypes type;
public: 
	ast::PrimitiveTypes get();
	PrimitiveType(ast::PrimitiveTypes type);
};

