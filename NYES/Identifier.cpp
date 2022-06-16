#include "Identifier.h"
std::string Identifier::get()
{
	return "f";
}

Identifier::Identifier(std::string token, IdentifierType type)
{
	this->token = token;
	this->type = type;
}