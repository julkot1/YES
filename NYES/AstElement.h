#pragma once
#include <string>
#include <queue>
#include "ast.h"

class AstElement
{
private:
	std::string token;
	int line;
	int column;
	ast::AstElementType astType;
public:
	std::string getToken();
	int getLine();
	int getColumn();
	std::string getcode();
	ast::AstElementType getElementType();
	AstElement(int line, int column, std::string token, ast::AstElementType astType);
	AstElement();
};

