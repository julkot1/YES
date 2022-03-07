package pl.julkot1.yes.ast.models;

import lombok.Getter;
import lombok.Setter;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.List;
@Getter
public class Argument implements IParental<Argument>{
    public Argument(String token, long line) {
        this.token = token;
        this.line = line;
        type = Type.NULL;
        prefixes = new ArrayList<>();
    }

    public Argument(String token, long line, Argument parent){
        this.token = token;
        this.parent  = parent;
        this.line = line;
        type = Type.NULL;
        prefixes = new ArrayList<>();

    }
    @Setter
    protected String namespace = "_GLOBAL";
    protected Type type;
    public void setType(Type type){
        if(type == null)this.type = Type.NULL;
        else this.type = type;
    }
    @Setter
    private Argument parent;
    protected final String token;
    protected final List<PrefixTokens> prefixes;
    private final long line;

    @Override
    public void addToParent(Argument argument) {

    }

    @Override
    public Argument get() {
        return this;
    }
}
