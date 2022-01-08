package pl.julkot1.yes.ast.resolver;

import pl.julkot1.yes.ast.models.IParental;
import pl.julkot1.yes.lexer.tokens.Token;

public class StatementResolver implements Resolver{

    @Override
    public int resolve(Token token, IParental<?> parent, int j) {
        return 0;
    }
}
