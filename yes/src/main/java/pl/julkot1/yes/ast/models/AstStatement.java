package pl.julkot1.yes.ast.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AstStatement extends Argument{

    private List<Argument> arguments;
    public AstStatement(String token,long line) {
        super(token, line);
        arguments = new ArrayList<Argument>();
    }
    public void addArgument(Argument argument) {
        arguments.add(argument);
    }
    public Argument getArgument(int index){
        return arguments.get(index);
    }
}
