#pragma once
#include <string>
#include <queue>

class AstElement
{
private:
	std::string token;
	int line;
	int column;
public:
	std::string getToken();
	int getLine();
	int getColumn();
	std::string getcode();
	AstElement(int line, int column, std::string token);
	AstElement();
};

