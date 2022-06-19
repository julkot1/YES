#pragma once
#include "Identifier.h"
#include "AstInterface.h"

class CFunction :
    public Identifier
{
public:
    CFunction(std::string token, std::string code, AstInterface* cInterface);
    std::string get();
    AstInterface* getInterface();
private:
    std::string code;
    AstInterface* cInterface;
    
};

