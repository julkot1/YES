#include <string>
#include "lexer.h"
#include "Keywords.h"
#include <regex>
#include <functional>

namespace lexer
{
	/*
	* 	if (getToken(c_str) == ENDL 
					|| getToken(str) == FUNCTION_OPEN 
					|| getToken(str) == FUNCTION_CLOSE
					|| getToken(str) == GENERIC_CLOSE
					|| getToken(str) == GENERIC_OPEN
					|| getToken(str) == INTERFACE_CLOSE
					|| getToken(str) == INTERFACE_OPEN
					|| getToken(str) == TYPE_OPEN
					|| getToken(str) == TYPE_CLOSE)
	*/
	bool isFunctionToken(std::string str)
	{
		return getToken(str) == ENDL
			|| getToken(str) == FUNCTION_OPEN
			|| getToken(str) == FUNCTION_CLOSE;
	}
	bool isTypeToken(std::string str)
	{
		return getToken(str) == TYPE_OPEN
			   || getToken(str) == TYPE_CLOSE;
	}
	bool isInterfaceToken(std::string str)
	{
		return getToken(str) == INTERFACE_OPEN
			|| getToken(str) == INTERFACE_CLOSE;
	}
	bool isGenericToken(std::string str)
	{
		return getToken(str) == GENERIC_OPEN
			|| getToken(str) == GENERIC_CLOSE;
	}
	bool isScopeToken(std::string str)
	{
		return isTypeToken(str) || isInterfaceToken(str) || isGenericToken(str) || isFunctionToken(str);
	}
	Token* addToken(int line, int column, Tokens type, GrammarScopes scope)
	{
		return new Token(line, column, getTokenStr(type), scope);
	}
	Token* addToken(int line, int column, std::string str, GrammarScopes scope)
	{
		return new Token(line, column, str, scope);
	}
	Tokens getToken(std::string token) 
	{
		if (token.compare(";") == 0) return ENDL;
		if (token == "\"") return STRING_OPEN;
		if (token == "{") return FUNCTION_OPEN;
		if (token == "}") return FUNCTION_CLOSE;
		if (token == "[") return INTERFACE_OPEN;
		if (token == "]") return INTERFACE_CLOSE;
		if (token == "(") return TYPE_OPEN;
		if (token == ")") return TYPE_CLOSE;
		if (token == "<") return GENERIC_OPEN;
		if (token == ">") return GENERIC_CLOSE;
		if (token == "@") return TYPE_PTR;
		if (token == "$") return TYPE_GET;
		if (token == "!") return TYPE_CONST;
		return VALUE;
	}
	std::string getTokenStr(Tokens token)
	{
		if (token == ENDL)return";";
		if (token == STRING_OPEN)return "\"";
		if (token == FUNCTION_OPEN)return "{";
		if (token == FUNCTION_CLOSE)return "}";
		if (token == INTERFACE_OPEN)return "[";
		if (token == INTERFACE_CLOSE)return "]";
		if (token == TYPE_OPEN)return "(";
		if (token == TYPE_CLOSE)return ")";
		if (token == GENERIC_OPEN)return "<";
		if (token == GENERIC_CLOSE)return ">";
		if (token == TYPE_CLOSE)return ")";
		if (token == TYPE_PTR)return "@";
		if (token == TYPE_GET)return "&";
		if (token == TYPE_CONST)return "!";
		return "unknown token";
	}
	Tokens valueType(std::string token)
	{
		if (Keywords::isType(token))return TYPE;
		if (Keywords::isLiteral(token)) return LITERAL;
		if (std::regex_match(token, std::regex("^[A-Za-z]+$"))) return IDENTIFIER;

		return LITERAL;
	}


	std::vector<Token*> lexer(std::string str)
	{
        std::string token = "";
        std::vector<Token*> tokens;
        lexer::GrammarScopes scope = lexer::GrammarScopes::FUNCTION_SCOPE;
		lexer::GrammarScopes prevScope = lexer::GrammarScopes::FUNCTION_SCOPE;
        int line = 1;
        int column = 0;
        for (char c : str) {
            if (c == '\n') {
                line++;
                column = 0;
            }
            column++;

			if (scope !=lexer::GrammarScopes::GENERIC_SCOPE)
				prevScope = scope;

            isStringScope(&scope, c, &token);

			if (scope == GrammarScopes::FUNCTION_SCOPE)scope = tokenizeFunction(&tokens, &token, c, line, column);
            isFunctionScope(&scope, c, line, column, &tokens);

			if (scope == GrammarScopes::TYPE_SCOPE)scope = tokenizeType(&tokens, &token, c, line, column);
			isTypeScope(&scope, c, line, column, &tokens);

			if (scope == GrammarScopes::GENERIC_SCOPE)scope = tokenizeGeneric(&tokens, &token, c, line, column);
			isGenericScope(&scope, prevScope, c, line, column, &tokens);

			if (scope == GrammarScopes::INTERFACE_SCOPE)scope = tokenizeInterface(&tokens, &token, c, line, column);
			isInterfaceScope(&scope, c, line, column, &tokens);


            if (scope == GrammarScopes::STRING_SCOPE)scope = tokenizeString(&tokens, &token, c, line, column);
            
            if (scope == GrammarScopes::EXPRESSION_SCOPE)scope = tokenizeExpression(&tokens, &token, c, line, column);
			

			
			auto c_str = std::string(1, c);
			if (token != "" && (getToken(c_str) == ENDL )  && scope != GrammarScopes::STRING_SCOPE)
			{
				tokens.push_back(addToken(line, column, token, prevScope));
				tokens.push_back(addToken(line, column, c_str, GrammarScopes::FUNCTION_SCOPE));
				token = "";
			}
			else if (scope!=GrammarScopes::STRING_SCOPE)
			{
				if (isScopeToken(c_str))
				{
					if(isFunctionToken(c_str))scope = GrammarScopes::FUNCTION_SCOPE;
					else if (isGenericToken(c_str))scope = GrammarScopes::GENERIC_SCOPE;
					else if (isTypeToken(c_str))scope = GrammarScopes::TYPE_SCOPE;
					else if (isInterfaceToken(c_str))scope = GrammarScopes::INTERFACE_SCOPE;
					tokens.push_back(addToken(line, column, getToken(c_str), scope));
				}
			}
			
        }
		if (token != "")
		{
			tokens.push_back(addToken(line, column, token, scope));
		}
        return tokens;
	}
}
