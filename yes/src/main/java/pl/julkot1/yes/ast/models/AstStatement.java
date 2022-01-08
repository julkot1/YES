package pl.julkot1.yes.ast.models;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AstStatement extends Argument{

    private final List<Argument> arguments;
    public AstStatement(String token,long line,Argument  a) {
        super(token, line, a);
        arguments = new ArrayList<>();
    }
    public void addArgument(Argument argument) {
        arguments.add(argument);
    }
    public Argument getArgument(int index){
        return arguments.get(index);
    }


}
