#pragma once
#include <string>
#include <queue>
#include <../../ast.h>
#include <../../AstElement.h>

class AstGeneric : public AstElement {
private:
	ast::GenericType genericType;
	std::vector<std::string> literals;
public:
	ast::GenericType getGenericType();
	std::vector<std::string> getLiterals();
	AstGeneric();
	AstGeneric(int line, int column, std::string token, ast::GenericType genericType);
};
