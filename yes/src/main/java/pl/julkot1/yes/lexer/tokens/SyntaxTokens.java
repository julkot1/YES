package pl.julkot1.yes.lexer.tokens;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum SyntaxTokens {
    COMMENT('#'),
    METADATA('@'),
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
