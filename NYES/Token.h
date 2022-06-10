#pragma once
#include <string>
#include "Keywords.h"

using namespace lexer;
class Token
{
private:
	int line, column;
	std::string str;
    Tokens type;
	GrammarScopes scope;
	void setType();
public:

	Token(int line,int column, std::string str, lexer::GrammarScopes scope);
	int getLine();
	int getColumn();
	std::string getStr();
	Tokens getType();
	GrammarScopes getScope();
};

