#pragma once
#include <string>
#include <../../ast.h>
#include <../../AstElement.h>
#include <../../AstLiteral.h>
#include <../../AstGeneric.h>

class AstExpression : public AstElement {
private:
	std::queue<AstElement * > args;
	AstGeneric* genericType;
	
	AstType* type;
public:
	std::queue<AstElement *> getArgs();
	void addArg(AstElement* arg);
	void setGenericType(AstGeneric* genericType);
	AstGeneric* getGenericType();
	void setType(AstType* type);
	AstExpression(int line, int column, std::string token);
	~AstExpression();
	AstType* getType();

};