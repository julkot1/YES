package pl.julkot1.yes.ast.scope;

import pl.julkot1.yes.ast.builder.Builder;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.SyntaxTokens;
import pl.julkot1.yes.lexer.tokens.Token;
import pl.julkot1.yes.lexer.tokens.TokenType;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class StatementBuilder extends Builder<AstStatement> {
    @Override
    protected void build() throws InvalidYesSyntaxException {
       Argument argument = null;

       do{

           argument = getStatementArgument();
           if(argument!=null)inst.addArgument(argument);
       }while (argument!=null);
        scope.shift(1);
        scope.updateTokens();
    }
    private Argument getStatementArgument() throws InvalidYesSyntaxException {
        AtomicReference<Argument> argument = new AtomicReference<>();
        scope.iterate((t, prev, next, index) -> {
            boolean isNext = true;
            switch (t.type()){
                case PREFIX -> this.getPrefix(t);
                case TYPE -> this.setType(t);
                case VALUE -> argument.set(new Value(Type.INT, "4", 47, inst));
                case SYNTAX -> {
                    if(t.obj().equals(SyntaxTokens.END_LINE)) {
                        scope.shift(-1);
                        isNext = false;
                    };
                }
            }
            scope.shift(1);
            return isNext;
        });
        type = null;
        prefixes = new ArrayList<>();
        scope.updateTokens();
        return argument.get();

    }
    private Value buildValue(Token t, int index){
        return null;
    }
    @Override
    protected void createScope(Scope rawScope, int shift) throws InvalidYesSyntaxException {
        this.scope = rawScope;
        this.scope.shift(shift);
        this.scope.updateTokens();

        var first = this.scope.getTokens().get(0);
        if(!first.type().equals(TokenType.STATEMENT)) throw new InvalidYesSyntaxException(first.line(), "Nobody expect "+first+" token! (Statement should be better choice)");

        inst = new AstStatement(first.toString(), first.line(), this.scope.getParent());

        this.scope.shift(1);
        this.scope.updateTokens();
    }

}
