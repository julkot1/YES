#include "AstInterface.h"
AstInterface::AstInterface(int line, int column, std::string token) : AstElement(line, column, token, ast::AstElementType::INTERFACE)
{
	this->identifiers = new std::vector<AstLiteral*>();
};
AstInterface::AstInterface()
{
	this->identifiers = new std::vector<AstLiteral*>();
}
std::vector<AstLiteral*>* AstInterface::getIdentifiers()
{
	return this->identifiers;
}
void AstInterface::addIdentifier(AstLiteral* identifier)
{
	if(this->contains(identifier))throw;
	this->identifiers->push_back(identifier);
}
bool AstInterface::contains(AstLiteral* identifier)
{
	for (std::size_t i = 0; i < this->identifiers->size(); ++i) {
		if (this->identifiers->at(i)->getToken().compare(identifier->getToken()) == 0)return true;
	}
	return false;
}
AstInterface::~AstInterface()
{
	delete this->identifiers;
}