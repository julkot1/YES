#pragma once
#include <string>
#include "parser.h"
namespace yesc
{
	void compile(std::string name, AstFunction* ast);
	//void compileFunction(std::string name);
}