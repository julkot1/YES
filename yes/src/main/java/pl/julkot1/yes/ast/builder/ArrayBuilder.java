package pl.julkot1.yes.ast.builder;

import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.scope.Scope;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;

public class ArrayBuilder extends Builder<Array> {
    private int beginning;
    @Override
    protected void build() throws InvalidYesSyntaxException {
        var token = scope.getTokens().get(beginning);
        inst = new Array(type, token.toString(), token.line(), scope.getParent());
        var shift = IndexBuilder.parseIndex(inst, beginning,scope.getTokens());
        scope.shift(shift);
        scope.updateTokens();
    }

    @Override
    protected void createScope(Scope rawScope, int shift) throws InvalidYesSyntaxException {
        if(type==null) throw new InvalidYesSyntaxException(rawScope.getTokens().get(shift).line(), "type of array must be specified");
        scope = rawScope;
        beginning = shift;

    }
}
