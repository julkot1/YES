#pragma once
#include <vector>
#include <functional>
#include <regex>
#include <string>
#include "Token.h"
#include "Keywords.h"
namespace lexer
{
	
	
	Tokens getToken(std::string token);
	Tokens valueType(std::string token);
	std::string getTokenStr(Tokens token);
	std::vector<Token*> lexer(std::string str);

	bool isWhitespace(char c);

    void isStringScope(lexer::GrammarScopes* scope, char c, std::string* token);
	GrammarScopes tokenizeString(std::vector<Token*>* tokens, std::string* token, char c, int line, int column);

	void isFunctionScope(lexer::GrammarScopes* scope,  char c, int line, int column, std::vector<Token*> *tokens);
	GrammarScopes tokenizeFunction(std::vector<Token*>* tokens, std::string* token, char c, int line, int column);

	void isTypeScope(lexer::GrammarScopes* scope,  char c, int line, int column, std::vector<Token*> *tokens);
	GrammarScopes tokenizeType(std::vector<Token*>* tokens, std::string* token, char c, int line, int column);


	void isGenericScope(lexer::GrammarScopes* scope, lexer::GrammarScopes prevScope, char c, int line, int column, std::vector<Token*>* tokens);
	GrammarScopes tokenizeGeneric(std::vector<Token*>* tokens, std::string* token, char c, int line, int column);

	void isInterfaceScope(lexer::GrammarScopes* scope, char c, int line, int column, std::vector<Token*>* tokens);
	GrammarScopes tokenizeInterface(std::vector<Token*>* tokens, std::string* token, char c, int line, int column);

	GrammarScopes tokenizeExpression(std::vector<Token*>* tokens, std::string* token, char c, int line, int column);
}

