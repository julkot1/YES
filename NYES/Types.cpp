#include "Types.h"
namespace type
{
	std::string getPrimitiveC(ast::PrimitiveTypes token)
	{
		switch (token)
		{
		case ast::SHORT:
			return "short";
			break;
		case ast::CHAR:
			return "char";
			break;
		case ast::INT:
			return "int";
			break;
		case ast::STR:
			return "char*";
			break;
		case ast::LONG:
			return "long";
			break;
		case ast::FLOAT:
			return "float";
			break;
		case ast::BOOLEAN:
			return "bool";
			break;
		case ast::DOUBLE:
			return "double";
			break;
		case ast::NUM:
			return "int";
			break;
		default:
			return "NULL";
			break;
		}
	}
	std::string getComplex(AstType* type)
	{

		std::string CTypetoken = ""; 
		ast::PrimitiveTypes t = type->get();
		ast::PrimitiveTypes baseType = type->get();
		bool isConst = false, isDef = false, isPrimitive = false;
		
		if (baseType == ast::CFUN || baseType == ast::FUNCTION)throw;
		do{
			switch (t)
			{
			case ast::PTR: 
				if (isDef)throw;
				CTypetoken += "*"; 
				break;
			case ast::CONST:
				if (isConst||isDef)throw;
				isConst = true;
			break;
			case ast::DEF:
				if (isConst || isDef)throw;
				isDef = true;
				break;
			default:
				if (isPrimitive)throw;
				isPrimitive = true;
				if (baseType == ast::PTR) CTypetoken = getPrimitiveC(t) + CTypetoken;
				else CTypetoken += getPrimitiveC(t);
				break;
			}
		} while ((t = type->nextChild()) != ast::EMPTY);
		if (isConst)CTypetoken = "const " + CTypetoken;
		return CTypetoken;
	}
	
	std::string getPrimitive(AstType* type)
	{
		ast::PrimitiveTypes token = type->get();
		return getPrimitiveC(token);
	}
	std::string toCtoken(AstType* type)
	{
		if (type->isComplexType())return getComplex(type);
		return getPrimitive(type);
	}
	bool isStr(std::string token)
	{
		return token[0] == '\"' && token[token.size() - 1] == '\"';
	}

	bool isNumber(std::string token)
	{
		return !token.empty() && std::find_if(token.begin(),
			token.end(), [](unsigned char c) { return !std::isdigit(c); }) == token.end();
	}
	bool isBool(std::string token)
	{
		return token.compare("true") == 0 || token.compare("false") == 0;
	}
	bool isFloatNumber(const std::string& string) {
		std::string::const_iterator it = string.begin();
		bool decimalPoint = false;
		int minSize = 0;
		if (string.size() > 0 && (string[0] == '-' || string[0] == '+')) {
			it++;
			minSize++;
		}
		while (it != string.end()) {
			if (*it == '.') {
				if (!decimalPoint) decimalPoint = true;
				else break;
			}
			else if (!std::isdigit(*it) && ((*it != 'f') || it + 1 != string.end() || !decimalPoint)) {
				break;
			}
			++it;
		}
		return string.size() > minSize && it == string.end();
	}
	bool isLiteral(std::string token)
	{
		return isBool(token) || isStr(token) || isNumber(token);
	}
	AstType* getAstType(std::string token)
	{
		auto type = new AstType();
		if (isBool(token)) type->setType(ast::PrimitiveTypes::BOOLEAN);
		if (isNumber(token))type->setType(ast::PrimitiveTypes::INT);
		if (isFloatNumber(token))type->setType(ast::PrimitiveTypes::FLOAT);
		if (isStr(token))type->setType(ast::PrimitiveTypes::STR);
		return type;
	}
}