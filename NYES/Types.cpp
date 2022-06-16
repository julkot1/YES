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
}