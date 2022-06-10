#include "parser.h"
#include "lexer.h"
#include <stdexcept>
#include <stack>
AstExpression* newExpression(Token *token) 
{
	auto expr = new AstExpression(token->getLine(), token->getColumn(), token->getStr());
	return expr;
}
AstLiteral* newLiteral(Token* token) 
{
	auto lit = new AstLiteral(token->getLine(), token->getColumn(), token->getStr());
	return lit;
}
AstFunction* newFunction(Token* token) 
{
	auto fun = new AstFunction(token->getLine(), token->getColumn(), token->getStr());
	return fun;
}
AstType* newType(Token* token)
{
	auto type = new AstType(token->getLine(), token->getColumn(), token->getStr());
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
				expr.push(newExpression(token));
			else if (token->getScope() == lexer::GrammarScopes::EXPRESSION_SCOPE)
			{
				expr.top()->addArg(newLiteral(token));
			}
			//else throw std::exception("scope exception");
			break;
		case lexer::Tokens::LITERAL:
			expr.top()->addArg(newLiteral(token));
			break;
		case lexer::ENDL:
			functions.top()->addExpr(expr.top());
			expr.pop();
			break;
		case lexer::Tokens::FUNCTION_OPEN:
			functions.push(newFunction(token));
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
			if (type != NULL)throw std::exception("invalid type declaration");
			//type->
			break;
		case lexer::Tokens::TYPE_CLOSE:
			if (type == NULL)throw std::exception("invalid type declaration");
			typeEnd = true;
			break;
		}

		
	}
	return _GLOBAL;
};
