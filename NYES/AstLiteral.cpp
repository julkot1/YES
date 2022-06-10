#include "AstLiteral.h"
#include "AstType.h"
AstLiteral::AstLiteral(int line, int column, std::string token) : AstElement(line, column, token, ast::AstElementType::LITERAL)
{}
void AstLiteral::setType(AstType *type) {
	this->type = type;
}
AstType* AstLiteral::getType() {
	return type;
}