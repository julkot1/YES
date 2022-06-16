#include <fstream>
#include <iostream>
#include <vector>
#include "lexer.h"
#include <sstream> 
#include "parser.h"
#include "transcompiler.h"
#include "Environment.h"
#include "Types.h"
using std::string;
using std::vector;

int main()
{
    std::ifstream file;
    file.open("program.yes");
    vector<Token*> tokens;
    if (file.good())
    {
        std::stringstream strStream;
        strStream << file.rdbuf();
        string code = strStream.str();
        tokens = lexer::lexer(code);
        auto ast = parse(tokens);
        auto args =  ast->getArgs()->back()->getArgs();
        args->pop();
        auto fun = static_cast<AstFunction*>(args->back());
        yesc::compile("program", ast);
        for (Token* t : tokens)delete t;
        delete ast;
    }
    file.close();
    return 0;
}