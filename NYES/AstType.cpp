#include "AstType.h"
AstType::AstType()
{
	this->isComplex = false;
	this->type = ast::PrimitiveTypes::NONE;
}
AstType::AstType(int line, int column, std::string token) : AstElement(line, column, token, ast::AstElementType::TYPE)
{
	this->isComplex = false;
	this->type = ast::PrimitiveTypes::NONE ;
};
void AstType::setType(ast::PrimitiveTypes type)
{
	if (this->type ==  ast::PrimitiveTypes::NONE)
	{
		this->type = type;
		this->isComplex = ast::isComplexType(type);
	}
	else if (this->isComplex)
	{
		this->children.push_back(type);
	}
	else throw;
};


ast::PrimitiveTypes AstType::get()
{
	return this->type;
}
ast::PrimitiveTypes AstType::nextChild()
{
	if (this->children.size() == 0)return ast::EMPTY;
	auto temp = this->children.back();
	this->children.pop_back();
	return temp;
}
bool AstType::isComplexType()
{
	return this->isComplex;
}