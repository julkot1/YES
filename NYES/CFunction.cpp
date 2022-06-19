#include "CFunction.h"

CFunction::CFunction(std::string token, std::string code, AstInterface* cInterface) : Identifier(token, IdentifierType::CFUN)
{
	this->code = code;
	this->cInterface = cInterface;
}
AstInterface* CFunction::getInterface()
{
	return this->cInterface;
}
std::string CFunction::get()
{
	return this->code;
}