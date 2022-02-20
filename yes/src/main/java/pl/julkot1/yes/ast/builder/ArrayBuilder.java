package pl.julkot1.yes.ast.builder;

import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.scope.Scope;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.SpecialTypeTokens;
import pl.julkot1.yes.lexer.tokens.SyntaxTokens;

public class ArrayBuilder extends Builder<Array> {
    private int beginning;
    @Override
    protected void build() throws InvalidYesSyntaxException {
        var token = scope.getTokens().get(beginning);
        inst = new Array(type, token.toString(), token.line(), scope.getParent());
        addPrefixes(inst);
        if(scope.getTokens().size()>beginning+1){
            if(!scope.getTokens().get(beginning+1).obj().equals(SyntaxTokens.REGISTER_OPEN)){
                throw new InvalidYesSyntaxException(token.line(), "missing array index");
            }
        }
        var shift = IndexBuilder.parseIndex(inst, beginning,scope.getTokens());
        scope.shift(shift);
        scope.updateTokens();
    }

    @Override
    protected void createScope(Scope rawScope, int shift)  {
        scope = rawScope;
        beginning = shift;

    }
}
