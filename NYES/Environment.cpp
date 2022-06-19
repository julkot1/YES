#include "Environment.h"

void Environment::addIdentifier(Identifier* identifier)
{
	if (this->contains(identifier->getToken()))throw;
	this->identifiers->push_back(identifier);
}
Identifier* Environment::get(std::string identifier)
{
	for (Identifier* i : *(this->identifiers))
	{
		if(i->getToken().compare(identifier)==0)return i;
	}
	if (this->parent != NULL) return this->parent->get(identifier);
	else throw;

}
bool Environment::contains(std::string identifier)
{
	if (this->identifiers->size() == 0)
	{
		if(this->parent == NULL)return false;
		return this->parent->contains(identifier);
	}
	else
	{
		for (Identifier* i : *(this->identifiers))
		{
			if (i->getToken().compare(identifier) == 0)return i;
		}
		if (this->parent != NULL)return this->parent->contains(identifier);
		else return false;
	}


}
Environment::Environment(Environment* parent)
{
	identifiers = new std::vector<Identifier*>();
	this->parent = parent;
}
Environment::Environment()
{
	identifiers = new std::vector<Identifier*>();
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