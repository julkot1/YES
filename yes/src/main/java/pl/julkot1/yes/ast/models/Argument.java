package pl.julkot1.yes.ast.models;

import lombok.Getter;
import lombok.Setter;
import pl.julkot1.yes.lexer.PrefixTokens;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.List;
@Getter
public class Argument {
    public Argument(String token, long line, Argument parent){
        this.token = token;
        this.parent  = parent;
        this.line = line;
        type = Type.NULL;
        prefixes = new ArrayList<>();

    }
    @Setter
    protected Type type;
    @Setter
    private Argument parent;
    protected final String token;
    protected final List<PrefixTokens> prefixes;
    private final long line;
}
