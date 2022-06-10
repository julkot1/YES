#include "lexer.h"

namespace lexer
{
    bool isWhitespace(char c)
    {
        return  c == ' ' || c == '\n' || c == '\t' || c == EOF;
    }

    void isStringScope(lexer::GrammarScopes* scope, char c, std::string* token)
    {

        if (*scope == GrammarScopes::STRING_SCOPE)return;
        if (getToken(std::string(1,c)) == STRING_OPEN) {
            *scope = GrammarScopes::STRING_SCOPE;
            *token = "";
        }


    }
    GrammarScopes tokenizeString(std::vector<Token*>* tokens, std::string* token, char c, int line, int column)
    {
        if (lexer::getToken(std::string(1, c)) == lexer::STRING_OPEN && token->length() > 0)
        {
            if (token->back() == '\\')
            {
                *token += c; return STRING_SCOPE;
            }
            Token* t = new Token(line,column, *token + '\"', GrammarScopes::EXPRESSION_SCOPE);
            tokens->push_back(t);
            *token = "";
            return  GrammarScopes::EXPRESSION_SCOPE;
        }
        else *token += c;
        return STRING_SCOPE;
    }

    void isFunctionScope(lexer::GrammarScopes* scope, char c, int line, int column, std::vector<Token*>* tokens)
    {
        if (*scope == GrammarScopes::STRING_SCOPE)return;
        Tokens token = getToken(std::string(1, c));
        if (token == ENDL || token == FUNCTION_OPEN)
            *scope = GrammarScopes::FUNCTION_SCOPE;
        if (token == FUNCTION_CLOSE)
            *scope = GrammarScopes::EXPRESSION_SCOPE;
    }


    GrammarScopes tokenizeFunction(std::vector<Token*>* tokens, std::string* token, char c, int line, int column)
    {
        if (lexer::getToken(std::string(1, c)) != VALUE ||
            lexer::getToken(*token) != VALUE ||
           isWhitespace(c))
        {
            if (*token != "")
            {
                Token* t = new Token(line, column, *token, GrammarScopes::FUNCTION_SCOPE);
                tokens->push_back(t);
                *token = "";
                return  GrammarScopes::EXPRESSION_SCOPE;
            }
        }
        else *token += c;
        return GrammarScopes::FUNCTION_SCOPE;
    }


    GrammarScopes tokenizeExpression(std::vector<Token*>* tokens, std::string* token, char c, int line, int column)
    {
        if (lexer::getToken(std::string(1, c)) != VALUE ||
            isWhitespace(c))
        {
            if (*token != "\"" && *token != "")
            {
                Token* t = new Token(line, column,  *token , GrammarScopes::EXPRESSION_SCOPE);
                tokens->push_back(t);
                *token = "";
                if (lexer::getToken(std::string(1, c)) == ENDL)return  GrammarScopes::FUNCTION_SCOPE;
                return  GrammarScopes::EXPRESSION_SCOPE;
            }
        }
        else *token += c;
        return GrammarScopes::EXPRESSION_SCOPE;
    }

    void isTypeScope(lexer::GrammarScopes* scope, char c, int line, int column, std::vector<Token*>* tokens)
    {
        if (*scope == GrammarScopes::STRING_SCOPE)return;
        Tokens token = getToken(std::string(1, c));
        if (token == TYPE_OPEN)
            *scope = GrammarScopes::TYPE_SCOPE;
        if (token == TYPE_CLOSE)
            *scope = GrammarScopes::EXPRESSION_SCOPE;
    }


    GrammarScopes tokenizeType(std::vector<Token*>* tokens, std::string* token, char c, int line, int column)
    {
        Tokens t = lexer::getToken(std::string(1, c));
        if (t != VALUE ||
            isWhitespace(c))
        {
            if (*token != "\"" && *token != "")
            {
                Token* t = new Token(line, column, *token, GrammarScopes::TYPE_SCOPE);
                tokens->push_back(t);
                *token = "";      
                return  GrammarScopes::TYPE_SCOPE;
            }
        }
        else *token += c;
        return GrammarScopes::TYPE_SCOPE;
    }


    void isGenericScope(lexer::GrammarScopes* scope, lexer::GrammarScopes prevScope, char c, int line, int column, std::vector<Token*>* tokens)
    {
        if (*scope == GrammarScopes::STRING_SCOPE)return;
        Tokens token = getToken(std::string(1, c));
        if (token == GENERIC_OPEN)
            *scope = GrammarScopes::GENERIC_SCOPE;
        if (token == GENERIC_CLOSE)
            *scope =prevScope;
    }
    GrammarScopes tokenizeGeneric(std::vector<Token*>* tokens, std::string* token, char c, int line, int column)
    {

        Tokens t = lexer::getToken(std::string(1, c));
        if (t != VALUE ||
            isWhitespace(c))
        {
            if (*token != "\"" && *token != "")
            {
                Token* t = new Token(line, column, *token, GrammarScopes::GENERIC_SCOPE);
                tokens->push_back(t);
                *token = "";
                return  GrammarScopes::GENERIC_SCOPE;
            }
        }
        else *token += c;
        return GrammarScopes::GENERIC_SCOPE;
    }

    void isInterfaceScope(lexer::GrammarScopes* scope, char c, int line, int column, std::vector<Token*>* tokens)
    {
        if (*scope == GrammarScopes::STRING_SCOPE)return;
        Tokens token = getToken(std::string(1, c));
        if (token == INTERFACE_OPEN)
            *scope = GrammarScopes::INTERFACE_SCOPE;
        if (token == INTERFACE_CLOSE)
            *scope = GrammarScopes::EXPRESSION_SCOPE;
    }
    GrammarScopes tokenizeInterface(std::vector<Token*>* tokens, std::string* token, char c, int line, int column)
    {
        Tokens t = lexer::getToken(std::string(1, c));
        if (t != VALUE ||
            isWhitespace(c))
        {
            if (*token != "\"" && *token != "")
            {
                Token* t = new Token(line, column, *token, GrammarScopes::INTERFACE_SCOPE);
                tokens->push_back(t);
                *token = "";
                return  GrammarScopes::INTERFACE_SCOPE;
            }
        }
        else *token += c;
        return GrammarScopes::INTERFACE_SCOPE;
    }
    

}