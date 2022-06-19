#pragma once

#include<vector>
#include<string>
#include "Identifier.h"

class Environment
{
private:
	Environment* parent;
	std::vector<Identifier*> *identifiers;
public:
	void addIdentifier(Identifier* identifier);
	Environment* pop();
	Identifier* get(std::string);
	bool contains(std::string identifier);
	Environment(Environment* parent);
	Environment();
	~Environment();


};

