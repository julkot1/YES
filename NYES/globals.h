#pragma once
#include "Environment.h"
#include <fstream>
namespace globals 
{
	extern Environment* env;
	const std::string YES_PREFIX = "__YES_OBJ_";
	extern std::ofstream main;
	extern std::ofstream functionsHeader;
	extern std::ofstream functions;

}
