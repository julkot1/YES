#pragma once
#include <../../ast.h>
#include <../../AstElement.h>
#include <../../AstLiteral.h>
#include <../../AstGeneric.h>
#include <../../AstInterface.h>
#include <../../AstExpression.h>
class AstFunction : public AstElement {
private:
	std::queue<AstExpression*> expressions;
	AstGeneric* genericType;
	AstInterface* typeInterface;
	AstType* type;

public:
	AstFunction(int line, int column, std::string token);
	
	std::queue<AstExpression*> getArgs();
	void setGenericType(AstGeneric* genericType);
	AstGeneric* getGenericType();
	void setType(AstType* type);
	AstType* getType();
	void setTypeInterface(AstInterface* typeInterface);
	AstInterface* getTypeInterface();
	void addExpr(AstExpression* expr);

};