package pl.julkot1.yes.lexer;

import lombok.AllArgsConstructor;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public enum PrefixTokens {
    CALL_PARENT_CR("$", Arrays.asList(NestedStatement.class, AstStatement.class)),
    REFERENCE("&", List.of(Array.class));
    String token;
    List<Class<? extends Argument>> forArgs;
    public boolean hasPrefix(List<PrefixTokens> prefixes){
        var prefix = this;
        return prefixes.stream().anyMatch((s)->s.equals(prefix));
    }


}
