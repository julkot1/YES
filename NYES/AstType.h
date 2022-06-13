#pragma once
#include <string>
#include <queue>
#include <../../ast.h>
#include <../../AstElement.h>

class AstType : public AstElement
{
private:
	ast::PrimitiveTypes type;
	bool isComplex;
	std::vector<ast::PrimitiveTypes> children;
public:
	AstType();
	AstType(int line, int column, std::string token);
	void setType(ast::PrimitiveTypes type);
	bool isComplexType();
	ast::PrimitiveTypes get();
	ast::PrimitiveTypes nextChild();
};

