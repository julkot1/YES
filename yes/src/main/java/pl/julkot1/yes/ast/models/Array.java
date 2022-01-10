package pl.julkot1.yes.ast.models;


import lombok.Getter;
import lombok.Setter;
import pl.julkot1.yes.types.Type;

@Getter
public class Array extends Argument{
    @Setter
    private Argument index;
    public Array(Type type, String token, long line, Argument parent) {
        super(token, line, parent);
        this.type = type;
    }
}
