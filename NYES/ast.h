#pragma once
#include <string>
#include <queue>
#include "Keywords.h"
namespace ast
{
	enum PrimitiveTypes {
		SHORT, CHAR, INT, STR, LONG, FLOAT, BOOLEAN, DOUBLE, PTR, CONST, DEF, FUN, NONE, NUM, CFUN, EMPTY
	};
	bool isComplexType(PrimitiveTypes type);
	bool isPrimitiveType(PrimitiveTypes type);
	PrimitiveTypes getType(std::string str);
	/*
	* DELCARATION - assign generic <Int>
	* DEFINITION - new generic <T>
	*/
	enum GenericType {
		DELCARATION, DEFINITION
	};
	enum AstElementType {
		EXPRESSION, GENERIC, TYPE, LITERAL, INTERFACE, FUNCTION
	};
}



