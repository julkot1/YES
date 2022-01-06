package pl.julkot1.yes.lexer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SyntaxTokens {
    COMMENT("//"),
    END_LINE(";"),
    STRING("\""),
    NESTED_OPEN("{"),
    NESTED_END("}"),
    REGISTER_OPEN( "["),
    REGISTER_END( "]"),
    CAST_OPEN("("),
    CAST_END(")");
    String token;
}
