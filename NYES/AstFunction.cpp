#include "AstFunction.h"
#include "AstGeneric.h"

std::queue<AstExpression*> AstFunction::getArgs()
{
	return this->expressions;
}
void AstFunction::setGenericType(AstGeneric* genericType)
{
	this->genericType = genericType;
}
AstGeneric* AstFunction::getGenericType()
{
	return this->genericType;
}

void AstFunction::setType(AstType* type)
{
	this->type = type;
}
AstType* AstFunction::getType()
{
	return this->type;
}
void AstFunction::setTypeInterface(AstInterface* typeInterface)
{
	this->typeInterface = typeInterface;
}
AstInterface* AstFunction::getTypeInterface()
{
	return this->typeInterface;
}
AstFunction::AstFunction(int line, int column, std::string token) : AstElement(line, column, token, ast::AstElementType::FUNCTION)
{}
void AstFunction::addExpr(AstExpression* expr)
{
	this->expressions.push(expr);
}
/*AstFunction::~AstFunction() {
	delete this->genericType;
	delete this->type;
	delete this->typeInterface;
}*/
