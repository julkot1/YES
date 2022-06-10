#include "AstGeneric.h"

AstGeneric::AstGeneric(int line, int column, std::string token, ast::GenericType genericType) : AstElement(line, column, token)
{
	this->genericType = ast::GenericType::DEFINITION;
};
AstGeneric::AstGeneric()
{
	this->genericType = ast::GenericType::DEFINITION;

};