package pl.julkot1.yes.lexer.tokens;

import lombok.AllArgsConstructor;


import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;

@AllArgsConstructor
public enum SyntaxTokens {
    COMMENT('#'),
    END_LINE(';'),
    STRING('\"'),
    NESTED_OPEN('{'),
    NESTED_END('}'),
    REGISTER_OPEN('['),
    REGISTER_END(']'),
    CAST_OPEN('('),
    CAST_END(')');
    char token;
    public static Optional<SyntaxTokens> getToken(char token){
        var list = new ArrayList<>(EnumSet.allOf(SyntaxTokens.class));
        return list.stream().filter((s)->s.token==token).findAny();
    }
}
