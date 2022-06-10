#include "AstInterface.h"
AstInterface::AstInterface(int line, int column, std::string token) : AstElement(line, column, token, ast::AstElementType::INTERFACE)
{
};