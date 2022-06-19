#include "globals.h"
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
    /*
    auto cI = new AstInterface();

    auto la = new AstLiteral(10, 10, "a");
    auto ta = new AstType(10, 10, "(Int)");
    ta->setType(ast::PrimitiveTypes::INT);
    la->setType(ta);
    cI->addIdentifier(la);

    auto lb = new AstLiteral(10, 10, "b");
    auto tb = new AstType(10, 10, "(Int)");
    tb->setType(ast::PrimitiveTypes::INT);
    lb->setType(tb);
    cI->addIdentifier(lb);


    auto args = new std::queue<std::string>;
    args->push("10");
    args->push("5");
    auto  s = yesc::compileCString("(%[a]+%[b])", cI, args);*/

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
        yesc::compile("program", ast);
        for (Token* t : tokens)delete t;
        delete ast;
    }
    file.close();
    return 0;
}