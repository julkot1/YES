#include "defaultFunctions.h"
#include "Types.h"
#include "transcompiler.h"
#include "globals.h"
namespace fun
{
	void impl(std::ofstream* main, std::ofstream* functionsHeader, std::ofstream* functions, AstExpression* expr) 
	{
		std::string token = expr->getToken();
		if (token.compare(fun::let)==0)letImpl(main, functionsHeader, functions, expr);
		else if (token.compare(fun::code)==0)codeImpl(main, functionsHeader, functions, expr);
	}
	void codeImpl(std::ofstream* main, std::ofstream* functionsHeader, std::ofstream* functions, AstExpression* expr)
	{
		std::string val = expr->getArgs()->front()->getToken();
		if (!type::isStr(val))throw;
		val.erase(0,1);
		val.erase(val.size()-1, 1);
		*(main) << val << ";";
		expr->getArgs()->pop();
	}
	void letImpl(std::ofstream* main, std::ofstream* functionsHeader, std::ofstream* functions, AstExpression* expr)
	{
		std::string id = expr->getArgs()->front()->getToken();
		auto type = static_cast<AstLiteral*>(expr->getArgs()->front())->getType(); expr->getArgs()->pop();
		if (type->get() == ast::CFUN) 
		{

		}
		else if (type->get() == ast::PrimitiveTypes::FUN)
		{

		}
		else
		{
			std::string val = expr->getArgs()->front()->getToken(); expr->getArgs()->pop();
			globals::env->addIdentifier(id);
			*(main) << type::toCtoken(type) << " " << id << "=" << val << ";";
		}
		
	}
}