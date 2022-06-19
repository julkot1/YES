#include "transcompiler.h"
#include "parser.h"
#include "defaultFunctions.h"
#include "globals.h"
#include <string>
#include <regex>
#include "Types.h"
namespace yesc
{
	std::string compileArg(AstElement* element)
	{
		if (element->getElementType() == ast::AstElementType::LITERAL)
		{
			
			auto lit = static_cast<AstLiteral*>(element);
			if(lit->getType()==NULL)lit->setType(type::getAstType(lit->getToken()));

			if (type::isLiteral(lit->getToken()))return lit->getToken();
			else if (globals::env->contains(lit->getToken()))return globals::YES_PREFIX + lit->getToken();
			
		}
		return "a";
	}
	std::string getVar(std::string str)
	{
		return std::string(str.begin() + 2, str.end() - 1);
	}

	std::string compileCString(std::string str, AstInterface* cInterface, std::queue<AstElement *>* args)
	{
		std::regex rex(R"(%\[\w+\])");
		std::smatch m;
		std::regex_search(str, m, rex);
		std::string codeC = "";
		while (std::regex_search(str, m, rex)) {
			if (!cInterface->contains(getVar(m[0])))throw;
			codeC += m.prefix();
			codeC += compileArg(args->front()); args->pop();
			str = m.suffix();
		}
		codeC += str;
		return codeC;
		
	}
	void openMain( std::string name)
	{
		globals::main << "#include\"" << name << "_fun.h" << "\"\n";
		globals::main << "int main(){";
	}
	void closeMain()
	{
		globals::main << "return 0;";
		globals::main << "}";
	}
	void openFunctions(std::string name)
	{
		globals::functions << "#include\"" << name << "_fun.h" << "\"\n";
	}
	void writeExpr(AstExpression* expr)
	{
		fun::impl(expr);
	}
	void compile(std::string name, AstFunction* ast)
	{

		globals::main.open("out/"+name + "_main.c", std::ofstream::out | std::ofstream::trunc);
		globals::functionsHeader.open("out/" + name + "_fun.h", std::ofstream::out | std::ofstream::trunc);
		globals::functions.open("out/" + name + "_fun.c", std::ofstream::out | std::ofstream::trunc);

		openFunctions(name);

		openMain(name);

		while (!ast->getArgs()->empty())
		{
			AstExpression* el = ast->getArgs()->front();
			writeExpr(el);
			ast->getArgs()->pop();
		}

		closeMain();
		globals::main.close();
		globals::functionsHeader.close();
		globals::functions.close();
		delete globals::env;

	}
}