#pragma once
#include<set>
#include<string>
class Environment
{
private:
	Environment* parent;
	std::set<std::string> *identifiers;
public:
	void addIdentifier(std::string identifier);
	Environment* pop();
	std::string get(std::string);
	bool contains(std::string identifier);
	Environment(Environment* parent);
	Environment();
	~Environment();


};

