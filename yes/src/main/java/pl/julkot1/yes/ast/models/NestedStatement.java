package pl.julkot1.yes.ast.models;

import lombok.Getter;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NestedStatement extends Argument{
    private final List<Argument> stack;
    public NestedStatement(Type type, long line, Argument parent) {
        super(null, line, parent);
        stack = new ArrayList<>();
        this.type = type;

    }

    @Override
    public void addToParent(Argument argument) {
        stack.add(argument);
    }
}
