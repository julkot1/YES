#pragma once
#include<string>
#include"AstType.h"

enum IdentifierType 
{
	VAR, CFUN, FUN
};
class Identifier
{
private:
	std::string token;
	AstType* astType;
	IdentifierType type;
public:
	std::string get();
	std::string getToken();
	void setAstType(AstType* t);
	AstType* getAstType();
	IdentifierType getType();
	Identifier(std::string token, IdentifierType type);
};

