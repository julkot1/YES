package pl.julkot1.yes.ast.models;

import lombok.Getter;
import lombok.Setter;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NestedStatement extends Argument{
    private final List<Argument> stack;
    @Getter @Setter
    private int nIndex = -1;
    @Getter @Setter
    private boolean isSingleStatement = true;
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
