#pragma once
#include <string>
#include <../../ast.h>
#include <../../AstElement.h>
#include <../../AstLiteral.h>


class AstInterface : public AstElement {
private:
	std::vector<AstLiteral *>* identifiers;

public:
	std::vector<AstLiteral*>* getIdentifiers();
	void addIdentifier(AstLiteral* identifier);
	bool contains(AstLiteral* identifier);
	AstInterface();
	AstInterface(int line, int column, std::string token);
	~AstInterface();
};
