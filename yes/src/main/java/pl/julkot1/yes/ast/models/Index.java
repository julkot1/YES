package pl.julkot1.yes.ast.models;

import lombok.Getter;
import lombok.Setter;

public class Index extends Argument{
    public Index( long line) {
        super("index", line);
    }
    public Index( long line, Argument argument) {
        super("index", line);
        this.argument = argument;
    }
    @Getter @Setter
    private Argument argument;
}
