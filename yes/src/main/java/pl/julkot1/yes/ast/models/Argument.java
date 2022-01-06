package pl.julkot1.yes.ast.models;

import lombok.Getter;
import lombok.Setter;
import pl.julkot1.yes.lexer.PrefixTokens;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.List;
@Getter
public class Argument {
    public Argument(String token, long line){
        this.token = token;
        this.line = line;
        type = Type.NULL;
        prefixes = new ArrayList<>();
    }
    @Setter
    protected Type type;
    protected final String token;
    protected final List<PrefixTokens> prefixes;
    private final long line;
}
