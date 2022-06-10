#pragma once
#include <string>
#include <queue>

namespace ast
{
	enum PrimitiveTypes {
		SHORT, CHAR, INT, STR, LONG, FLOAT, BOOLEAN, DOUBLE
	};
	enum TypeModifiers {
		POINTER, ADDRES, CONST
	};
	/*
	* DELCARATION - assign generic <Int>
	* DEFINITION - new generic <T>
	*/
	enum GenericType {
		DELCARATION, DEFINITION
	};
}



