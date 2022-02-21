package pl.julkot1.yes.statement.other;

import lombok.Getter;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.StatementTokens;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DoStatement extends Statement {
    public DoStatement(AstStatement astStatement) {
        super(astStatement);
        first = false;

    }
    public DoStatement(AstStatement astStatement, Type checkType, boolean first, String toCheck) {
        super(astStatement);
        this.checkType = checkType;
        this.first = first;
        this.toCheck = toCheck;
    }
    @Getter
    private Type checkType;
    private String toCheck;
    private final boolean first;
    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        var mess ="must be in MATCH";
        if(astStatement.getParent()==null){
            throw new InvalidYesSyntaxException(astStatement.getLine(), mess);
        }else{
            if(astStatement.getParent().getParent()==null)throw new InvalidYesSyntaxException(astStatement.getLine(), mess);
            else if(!astStatement.getParent().getParent().getToken().equals(StatementTokens.MATCH.getToken()))
                throw new InvalidYesSyntaxException(astStatement.getLine(), mess);
        }

        if(astStatement.getArguments().size()!=2)
            if(!astStatement.getType().equals(Type.NULL))
                throw new TypeException(astStatement.getLine(), astStatement.getToken(), "type must be not defined");
        var arg0 = astStatement.getArgument(0);
        var arg1 = astStatement.getArgument(1);
        if(!(arg1 instanceof NestedStatement))
            throw new InvalidYesSyntaxException(arg1.getLine(), "2nd argument must be a nested statement");
        if(arg0 instanceof Array && arg0.getType().equals(Type.NULL)){
            arg0.setType(checkType);
        }
        if(!arg0.getType().equals(checkType))
            throw new TypeException( arg0.getLine(),  arg0.getToken(), "type must be the same as MATCH 1st argument");


    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        if(!first)out.write("else ".getBytes());
        out.write(("if("+toCheck+"=="+arguments.get(0)+"){").getBytes());
        DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(1)), out);
        out.write("}".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(0)), out);
    }
}
