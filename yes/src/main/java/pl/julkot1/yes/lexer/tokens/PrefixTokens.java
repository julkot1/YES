package pl.julkot1.yes.lexer.tokens;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidPrefixUsageException;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;

import java.util.*;

@Getter
@AllArgsConstructor
public enum PrefixTokens {
    CALL_PARENT_CR('$', Arrays.asList(NestedStatement.class, AstStatement.class)),
    REFERENCE('&', List.of(Array.class));
    char token;
    List<Class<? extends Argument>> forArgs;
    public boolean hasPrefix(List<PrefixTokens> prefixes){
        var prefix = this;
        return prefixes.stream().anyMatch((s)->s.equals(prefix));
    }
    public static Optional<PrefixTokens> getToken(char token){
        var list = new ArrayList<>(EnumSet.allOf(PrefixTokens.class));
        return list.stream().filter((s)->s.token==token).findAny();
    }
    public void valid(Class<? extends Argument> clazz, Token t)  throws InvalidPrefixUsageException {
        if(!forArgs.contains(clazz))throw  new InvalidPrefixUsageException(t.line(), this.token+"", t.obj().toString());
    }

}
