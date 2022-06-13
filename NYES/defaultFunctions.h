#pragma once
#include <string>
#include <fstream> 
#include "parser.h"
namespace fun
{
	void impl(std::ofstream* main, std::ofstream* functionsHeader, std::ofstream* functions, AstExpression* expr);
	void letImpl(std::ofstream* main, std::ofstream* functionsHeader, std::ofstream* functions, AstExpression* expr);
	void codeImpl(std::ofstream* main, std::ofstream* functionsHeader, std::ofstream* functions, AstExpression* expr);
	const std::string let = "let";
	const std::string inlineC = "inlineC"; 
	const std::string code = "code";
}