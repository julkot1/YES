#pragma once
#include <../../ast.h>
#include <../../AstElement.h>
#include <../../AstType.h>

class AstLiteral : public AstElement {
private:
	AstType* type;
public:
	AstType* getType();
	void setType(AstType *type);
	AstLiteral(int line, int column, std::string token);
};

