#include "Token.h"
#include "lexer.h"

Token::Token(int line, int column, std::string str, lexer::GrammarScopes scope)
{
	this->column = column;
	this->line = line;
	this->scope = scope;
	this->str = str;
	this->setType();
}
void Token::setType()
{
	this->type = lexer::getToken(this->str);
	if (this->type == lexer::VALUE) this->type = lexer::valueType(this->str);
}
int Token::getLine() 
{
	return this->line;
}
int Token::getColumn()
{
	return this->column;
}
std::string Token::getStr()
{
	return this->str;
}
lexer::Tokens Token::getType()
{
	return this->type;
}
lexer::GrammarScopes Token::getScope()
{
	return this->scope;
}