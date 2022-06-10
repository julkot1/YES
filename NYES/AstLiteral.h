#pragma once
#include <../../ast.h>
#include <../../AstElement.h>
#include <../../AstType.h>

class AstLiteral : public AstElement {
private:
	AstType type;
public:
	ast::PrimitiveTypes getType();
	AstLiteral(int line, int column, std::string token);
	~AstLiteral();
};

