#pragma once
#include <string>
#include <../../ast.h>
#include <../../AstElement.h>
#include <../../AstLiteral.h>


class AstInterface : public AstElement {
private:
	std::vector<AstLiteral> identifiers;

public:
	std::vector<ast::TypeModifiers> getIdentifiers();
};
