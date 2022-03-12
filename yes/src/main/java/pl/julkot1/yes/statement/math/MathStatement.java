package pl.julkot1.yes.statement.math;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.DefaultTypes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class MathStatement extends Statement {
    protected String operator;

    public MathStatement(AstStatement astStatement, String operator) {
        super(astStatement);
        var argumentsTypes = DefaultTypes.argumentsToTypesList(this.astStatement.getArguments());
        this.astStatement.setType(DefaultTypes.getMathType(argumentsTypes));
        this.operator = operator;
    }

    @Override
    protected void validArguments(){
        boolean quantity = this.astStatement.getArguments().size() == 2;
        if(!quantity)throw new InvalidArgumentsQuantity(this.astStatement.getLine(), this.astStatement.getToken());

    }

    @Override
    protected void setReturning() {
        String ret = "("+arguments.get(0) +
                this.operator +
                arguments.get(1)+")";
        setReturning(ret);

    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        var resultType = this.astStatement.getType();
        out.write(String.format("*((%s *)rx) = ", resultType.getCToken()).getBytes());
        out.write(getReturning().getBytes());
        out.write(";".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        var args =  DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
        this.astStatement.setType(DefaultTypes.getMathType(DefaultTypes.argumentsToTypesList(this.astStatement.getArguments())));
        return args;
    }
}
