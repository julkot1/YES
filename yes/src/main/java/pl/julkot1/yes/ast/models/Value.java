package pl.julkot1.yes.ast.models;

import pl.julkot1.yes.types.Type;



public class Value extends Argument{
    public Value(Type type, String token, long line) {
        super(token, line);
        this.type = type;
    }
}
