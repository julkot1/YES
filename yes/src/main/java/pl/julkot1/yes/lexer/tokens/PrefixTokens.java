package pl.julkot1.yes.lexer.tokens;

import lombok.AllArgsConstructor;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;

import java.util.*;

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

}
