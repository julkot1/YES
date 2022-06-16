#include "Environment.h"

void Environment::addIdentifier(std::string identifier)
{
	if (this->contains(identifier))throw;
	this->identifiers->insert(identifier);
}
std::string Environment::get(std::string identifier)
{
	if (this->contains(identifier))throw;
	return "YES_VAR__" + identifier;

}
bool Environment::contains(std::string identifier)
{
	if (this->identifiers->size() == 0)
	{
		if(this->parent == NULL)return false;
		return this->parent->contains(identifier);
	}
	else if (this->identifiers->contains(identifier))return true;
	else 
	{
		if (this->parent != NULL)return this->parent->contains(identifier);
		else return false;
	}
}
Environment::Environment(Environment* parent)
{
	identifiers = new std::set<std::string>();
	this->parent = parent;
}
Environment::Environment()
{
	identifiers = new std::set<std::string>();
	this->parent = NULL;
}
Environment::~Environment()
{
	delete this->identifiers;
}
Environment* Environment::pop()
{
	if (this->parent == NULL)return this;
	Environment* env = this->parent;
	delete this;
	return env;
	
}