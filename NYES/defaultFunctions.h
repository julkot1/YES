#pragma once
#include <string>
#include <fstream> 
#include "parser.h"
namespace fun
{
	void impl( AstExpression* expr);
	void letImpl( AstExpression* expr);
	void codeImpl( AstExpression* expr);
	void envImpl(AstExpression* expr);
	const std::string let = "let";
	const std::string inlineC = "inlineC"; 
	const std::string code = "code";
}