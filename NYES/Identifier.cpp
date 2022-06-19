#include "Identifier.h"
std::string Identifier::getToken()
{
	return this->token;
}
std::string Identifier::get()
{
	return "__YES_OBJ_"+this->token;
}

Identifier::Identifier(std::string token, IdentifierType type)
{
	this->token = token;
	this->type = type;
}
void Identifier::setAstType(AstType* t)
{
	this->astType = t;
}
AstType* Identifier::getAstType()
{
	return this->astType;
}
IdentifierType Identifier::getType()
{
	return this->type;
}