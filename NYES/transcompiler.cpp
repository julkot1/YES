#include "transcompiler.h"
#include <fstream>
#include "parser.h"
#include "defaultFunctions.h"
#include "globals.h"
namespace yesc
{
	
	void openMain(std::ofstream* main, std::string name)
	{
		*main << "#include\"" << name << "_fun.h" << "\"\n";
		*main << "int main(){";
	}
	void closeMain(std::ofstream* main)
	{
		*main << "return 0;";
		*main << "}";
	}
	void openFunctions(std::ofstream* functions, std::string name)
	{
		*functions << "#include\"" << name << "_fun.h" << "\"\n";
	}
	void writeExpr(std::ofstream* main, std::ofstream* functionsHeader, std::ofstream* functions,AstExpression* expr)
	{
		fun::impl(main, functionsHeader, functions, expr);
	}
	void compile(std::string name, AstFunction* ast)
	{
		std::ofstream main;
		std::ofstream functionsHeader;
		std::ofstream functions;
		main.open("out/"+name + "_main.c", std::ofstream::out | std::ofstream::trunc);
		functionsHeader.open("out/" + name + "_fun.h", std::ofstream::out | std::ofstream::trunc);
		functions.open("out/" + name + "_fun.c", std::ofstream::out | std::ofstream::trunc);

		openFunctions(&functions, name);

		openMain(&main, name);

		while (!ast->getArgs()->empty())
		{
			AstExpression* el = ast->getArgs()->front();
			writeExpr(&main, &functionsHeader, &functions, el);
			ast->getArgs()->pop();
		}

		closeMain(&main);
		main.close();
		functionsHeader.close();
		functions.close();
		delete globals::env;
	}
}