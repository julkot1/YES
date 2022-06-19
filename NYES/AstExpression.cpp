#include "AstExpression.h"
AstExpression::AstExpression(int line, int column, std::string token) : AstElement(line, column, token, ast::AstElementType::EXPRESSION)
{
	this->args = new std::queue<AstElement*>();
}

AstExpression::~AstExpression() {
	delete this->genericType;
	delete this->type;
	delete this->args;
	delete this->typeInterface;
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
void AstExpression::setTypeInterface(AstInterface* typeInterface)
{
	this->typeInterface = typeInterface;
}
AstInterface* AstExpression::getTypeInterface()
{
	return this->typeInterface;
}
std::queue<AstElement *>* AstExpression::getArgs()
{
	return this->args;
}
void  AstExpression::addArg(AstElement* arg)
{
	this->args->push(arg);
}
