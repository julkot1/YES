package pl.julkot1.yes.ast;

import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.IParental;
import pl.julkot1.yes.lexer.tokens.SyntaxTokens;
import pl.julkot1.yes.lexer.tokens.Token;
import pl.julkot1.yes.lexer.tokens.TokenType;

import java.util.List;

public class AstBuilder {
    public AstBuilder(List<Token> tokens) {
        this.tokens = tokens;
        ast = new AST();
    }

    private final AST ast;
    private final List<Token> tokens;


    public AST getAst() {
        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);
            if(token.type().equals(TokenType.STATEMENT))i+=resolveStatement(token, ast, i);
        }
        return ast;
    }

    private int resolveStatement(Token token, IParental<?>  parent, int j) {

        var currentStatement = new AstStatement((String) token.obj(), token.line(), getParent(parent));
        for (int i = j+1; i < tokens.size(); i++) {
            var t = tokens.get(i);
            if(t.obj().equals(SyntaxTokens.END_LINE)){
                parent.addToParent(currentStatement);
                return i-j-1;
            }
        }
        return 0;
    }

    private Argument getParent(IParental<?> parent) {
        if(parent.get() instanceof AST)return null;
        return (Argument) parent.get();
    }
}
