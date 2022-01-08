package pl.julkot1.yes.ast.resolver;

import pl.julkot1.yes.ast.models.IParental;
import pl.julkot1.yes.lexer.tokens.Token;

@FunctionalInterface
public interface Resolver {
    int resolve(Token token, IParental<?> parent, int j);
}
