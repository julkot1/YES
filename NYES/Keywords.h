#pragma once
#include <string>
class Keywords
{
public:
	static const std::string BOOLEAN_TRUE;
	static const std::string BOOLEAN_FALSE;
	static const std::string SHORT;
	static const std::string CHAR;
	static const std::string INT;
	static const std::string STR;
	static const std::string LONG;
	static const std::string FLOAT;
	static const std::string BOOLEAN;
	static const std::string DOUBLE;
	static const std::string FUNCTION;
	static const std::string CONST;
	static const std::string PTR;
	static const std::string DEF;
	static bool isLiteral(std::string token);
	static bool isType(std::string token);
};

namespace lexer
{
	enum GrammarScopes
	{
		FUNCTION_SCOPE, TYPE_SCOPE, INTERFACE_SCOPE, GENERIC_SCOPE, STRING_SCOPE, EXPRESSION_SCOPE
	};
	enum Tokens
	{
		ENDL,
		STRING_OPEN,
		FUNCTION_OPEN,
		FUNCTION_CLOSE,
		TYPE_OPEN,
		TYPE_CLOSE,
		GENERIC_OPEN,
		GENERIC_CLOSE,
		INTERFACE_OPEN,
		INTERFACE_CLOSE,
		TYPE_PTR,
		TYPE_CONST,
		TYPE_GET,
		VALUE,
		LITERAL,
		TYPE,
		IDENTIFIER,
		FUNCTION
	};
};