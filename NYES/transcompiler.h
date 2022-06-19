#pragma once
#include <string>
#include "parser.h"
#include "Environment.h"
#include <fstream>
namespace yesc
{

	void compile(std::string name, AstFunction* ast); 
	std::string compileArg(AstElement* element);
	std::string compileCString(std::string str, AstInterface* cInterface, std::queue<AstElement*>* args);
}
