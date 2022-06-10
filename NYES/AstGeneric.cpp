#include "AstGeneric.h"

AstGeneric::AstGeneric(int line, int column, std::string token, ast::GenericType genericType) : AstElement(line, column, token, ast::AstElementType::GENERIC)
{
	this->genericType = ast::GenericType::DEFINITION;
};
AstGeneric::AstGeneric()
{
	this->genericType = ast::GenericType::DEFINITION;

};