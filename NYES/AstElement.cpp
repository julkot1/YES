#include "AstElement.h"

AstElement::AstElement(int line, int column, std::string token )
{
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
