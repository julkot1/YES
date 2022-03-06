package pl.julkot1.yes.lexer.tokens;

import pl.julkot1.yes.types.Type;

public record Token(Object obj, long line, TokenType type) {
    @Override
    public String toString() {
        return switch (type){
            case PREFIX -> ((PrefixTokens) this.obj).getToken()+"";
            case SYNTAX -> ((SyntaxTokens)obj).token+"";
            case VALUE, STATEMENT, TYPE, SPECIAL, ARRAY, NAMESPACE -> obj.toString();
        };

    }
}
