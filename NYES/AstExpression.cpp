#include "AstExpression.h"
AstExpression::AstExpression(int line, int column, std::string token) : AstElement(line, column, token, ast::AstElementType::EXPRESSION)
{}

AstExpression::~AstExpression() {
	delete this->genericType;
	delete this->type;
}

void AstExpression::setGenericType(AstGeneric* genericType)
{
	this->genericType = genericType;
}
AstGeneric* AstExpression::getGenericType()
{
	return this->genericType;
}

void AstExpression::setType(AstType* type)
{
	this->type = type;
}
AstType* AstExpression::getType()
{
	return this->type;
}
std::queue<AstElement *> AstExpression::getArgs()
{
	return this->args;
}
void  AstExpression::addArg(AstElement* arg)
{
	this->args.push(arg);
}