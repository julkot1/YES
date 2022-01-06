package pl.julkot1.yes.ast.models;

import lombok.Getter;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NestedStatement extends Argument{
    private final List<Argument> stack;
    public NestedStatement(Type type, String token, long line) {
        super(token, line);
        stack = new ArrayList<>();
        this.type = type;
    }
    public void addToStack(Argument argument) {
        stack.add(argument);
    }
}
