#pragma once
#include<string>
enum IdentifierType 
{
	VAR, CFUN, FUN
};
class Identifier
{
private:
	std::string token;
	IdentifierType type;
public:
	std::string get();
	Identifier(std::string token, IdentifierType type);
};

