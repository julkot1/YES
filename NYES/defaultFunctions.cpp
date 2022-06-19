#include "defaultFunctions.h"
#include "Types.h"
#include "CFunction.h"
#include "globals.h"
#include "transcompiler.h"
namespace fun
{
	void impl(AstExpression* expr) 
	{
		std::string token = expr->getToken();
		if (token.compare(fun::let) == 0)letImpl( expr);
		else if (token.compare(fun::code) == 0)codeImpl( expr);
		else if (globals::env->contains(token))envImpl(expr);
	}
	void envImpl(AstExpression* expr)
	{
		auto rawIdentifier = globals::env->get(expr->getToken());
		if (rawIdentifier->getType() == IdentifierType::CFUN)
		{
 			auto cfunId = static_cast<CFunction*>(rawIdentifier);
			globals::main <<yesc::compileCString(cfunId->get(), cfunId->getInterface(), expr->getArgs()) << ";";
		}
	}
	void codeImpl(AstExpression* expr)
	{
		std::string val = expr->getArgs()->front()->getToken();
		if (!type::isStr(val))throw;
		val.erase(0,1);
		val.erase(val.size()-1, 1);
		(globals::main) << val << ";";
		expr->getArgs()->pop();
	}
	void letImpl(AstExpression* expr)
	{
		std::string id = expr->getArgs()->front()->getToken();
		auto type = static_cast<AstLiteral*>(expr->getArgs()->front())->getType(); expr->getArgs()->pop();
		if (type->get() == ast::CFUN) 
		{
			if (expr->getArgs()->size() != 1)throw;
			auto fun = static_cast<AstFunction *>(expr->getArgs()->front()); expr->getArgs()->pop();
			if (fun->getElementType() != ast::AstElementType::FUNCTION)throw;
			if (fun->getArgs()->size() != 1)throw;
			auto arg1 = fun->getArgs()->front(); fun->getArgs()->pop();
			if (arg1->getToken().compare(fun::inlineC) != 0)throw;
			if (arg1->getArgs()->size() != 1)throw;
			if (fun->getTypeInterface() == NULL)throw;
			auto cStr = static_cast<AstLiteral*>(arg1->getArgs()->front()); arg1->getArgs()->pop();
			if (!type::isStr(cStr->getToken()))throw;
			auto cInterface = fun->getTypeInterface();
			auto str = cStr->getToken();
			auto identifier = new CFunction(id, std::string(str.begin()+1, str.end() - 1), cInterface);
			globals::env->addIdentifier(identifier);
		}
		else if (type->get() == ast::PrimitiveTypes::FUN)
		{

		}
		else
		{
			std::string val = expr->getArgs()->front()->getToken(); expr->getArgs()->pop();
			auto identifier = new Identifier(id, IdentifierType::VAR);
			identifier->setAstType(type);
			globals::env->addIdentifier(identifier);
			globals::main << type::toCtoken(type) << " " << globals::YES_PREFIX + id << "=" << val << ";";
		}
		
	}
}