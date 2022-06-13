#include "Types.h"
namespace type
{
	std::string getComplex(AstType* type)
	{
		//TODO
		return "NULL";
	}
	std::string getPrimitive(AstType* type)
	{
		ast::PrimitiveTypes token = type->get();
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
		return "NULL";
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
}