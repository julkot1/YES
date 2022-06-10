#pragma once
#include <string>
#include <queue>
#include <../../ast.h>
#include <../../AstElement.h>

class AstType : public AstElement
{
private:
	ast::PrimitiveTypes type;
	std::vector<ast::TypeModifiers> modifiers;
public:
	std::vector<ast::TypeModifiers> getModifiers();
	AstType(int line, int column, std::string token);
};

