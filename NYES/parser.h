#pragma once

#include "AstFunction.h"
#include "vector"
#include "Token.h"

AstFunction* parse(std::vector<Token*> tokens);