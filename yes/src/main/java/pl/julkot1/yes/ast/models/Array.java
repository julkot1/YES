package pl.julkot1.yes.ast.models;


import lombok.Getter;
import pl.julkot1.yes.types.Type;

@Getter
public class Array extends Argument{
    private final Argument index;
    public Array(Type type, String token,Argument index, long line, Argument parent) {
        super(token, line, parent);
        this.index = index;
        this.type = type;
    }
}
