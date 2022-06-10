#include "AstElement.h"
#include "ast.h"
AstElement::AstElement(int line, int column, std::string token, ast::AstElementType astType )
{
	this->astType = astType;
	this->column = column;
	this->line = line;
	this->token = token;
}
AstElement::AstElement()
{
	this->column = 0;
	this->line = 0;
	this->token = "";
}

std::string AstElement::getToken()
{
	return this->token;
}
int AstElement::getLine()
{
	return this->line;
}
int AstElement::getColumn()
{
	return this->column;
}
ast::AstElementType AstElement::getElementType()
{
	return this->astType;
}
