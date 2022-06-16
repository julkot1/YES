#include "parser.h"
#include "lexer.h"
#include <stdexcept>
#include <stack>


AstExpression* newExpression(Token *token, AstInterface* typeInterface)
{
	auto expr = new AstExpression(token->getLine(), token->getColumn(), token->getStr());
	if (typeInterface != NULL)expr->setTypeInterface(typeInterface);
	return expr;
}
AstLiteral* newLiteral(Token* token, AstType* type) 
{
	auto lit = new AstLiteral(token->getLine(), token->getColumn(), token->getStr());
	if (type != NULL)lit->setType(type);

	return lit;
}
AstFunction* newFunction(Token* token, AstInterface* typeInterface) 
{
	auto fun = new AstFunction(token->getLine(), token->getColumn(), token->getStr());
	if (typeInterface != NULL)fun->setTypeInterface(typeInterface);
	return fun;
}
AstType* newType(Token* token)
{
	auto type = new AstType(token->getLine(), token->getColumn(), token->getStr());
	return type;
}
AstInterface* newTypeInterface(Token* token)
{
	auto type = new AstInterface(token->getLine(), token->getColumn(), token->getStr());
	return type;
}
AstFunction* parse(std::vector<Token *> tokens)
{
	auto _GLOBAL = new AstFunction(0, 0, "_GLOBAL");
	std::stack<AstFunction*> functions;
	std::stack<AstExpression*> expr;
	AstInterface* typeInterface = NULL;
	bool typeInterfaceEnd = false;
	AstType* type = NULL;
	bool typeEnd = false;
	functions.push(_GLOBAL);

	for (Token* token : tokens)
	{
		switch (token->getType())
		{
		case lexer::Tokens::IDENTIFIER:
			if (token->getScope() == lexer::GrammarScopes::FUNCTION_SCOPE)
			{
				expr.push(newExpression(token, typeInterface));
				typeInterface = NULL;
			}		
			else if (token->getScope() == lexer::GrammarScopes::EXPRESSION_SCOPE && typeInterface == NULL)
			{
				expr.top()->addArg(newLiteral(token, type));
				type = NULL;
			}
			else if (typeInterface != NULL)
			{
				if (type == NULL) throw std::exception("Invalid interface identifier declaration");
				typeInterface->addIdentifier(newLiteral(token, type));
				type = NULL;
			}
			break;
		case lexer::Tokens::LITERAL:
			expr.top()->addArg(newLiteral(token, type));
			type = NULL;

			break;
		case lexer::ENDL:
			functions.top()->addExpr(expr.top());
			expr.pop();
			break;
		case lexer::Tokens::FUNCTION_OPEN:
			functions.push(newFunction(token, typeInterface));
			typeInterface = NULL;
			break;
		case lexer::Tokens::FUNCTION_CLOSE:
			expr.top()->addArg(functions.top());
			functions.pop();
			break;
		case lexer::Tokens::TYPE_OPEN:
			if(type != NULL)throw std::exception("invalid type declaration");
			typeEnd = false;
			type = newType(token);
			break;
		case lexer::Tokens::TYPE:
			if (type == NULL)throw std::exception("invalid type declaration");
			type->setType(ast::getType(token->getStr()));
			break;
		case lexer::Tokens::TYPE_CLOSE:
			if (type == NULL)throw std::exception("invalid type declaration");
			typeEnd = true;
			break;
		case lexer::Tokens::INTERFACE_OPEN:
			if (typeInterface != NULL)throw std::exception("invalid interface declaration");
			typeInterfaceEnd = false;
			typeInterface = newTypeInterface(token);
			break;
		case lexer::Tokens::INTERFACE_CLOSE:
			if (typeInterface == NULL)throw std::exception("invalid interface declaration");
			typeInterfaceEnd = true;
			break;
		}

		
	}
	return _GLOBAL;
};
