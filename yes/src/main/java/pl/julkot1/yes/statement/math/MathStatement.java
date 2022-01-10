package pl.julkot1.yes.statement.math;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.DefaultTypes;

import java.io.FileOutputStream;
import java.io.IOException;


public class MathStatement extends Statement {
    protected String operator;

    public MathStatement(AstStatement astStatement, String operator) {
        super(astStatement);
        this.operator = operator;
    }

    @Override
    protected void validArguments(){
        boolean quantity = this.astStatement.getArguments().size() == 2;
        if(!quantity)throw new InvalidArgumentsQuantity(this.astStatement.getLine(), this.astStatement.getToken());
        //TODO _INTERFACE type check
    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        var argumentsTypes = DefaultTypes.argumentsToTypesList(this.astStatement.getArguments());
        this.astStatement.setType(DefaultTypes.getMathType(argumentsTypes));
        var resultType = this.astStatement.getType();

        out.write(String.format("xr[ptx] = malloc(sizeof(%s));", resultType.getCToken()).getBytes());
        out.write(String.format("*((%s *)xr[ptx]) = ", resultType.getCToken()).getBytes());
        out.write(String.format("*((%s*)cr[0]) ", argumentsTypes.get(0).getCToken()).getBytes());
        out.write(this.operator.getBytes());
        out.write(String.format("*((%s*)cr[1]);", argumentsTypes.get(0).getCToken()).getBytes());
        out.write("ptx++;".getBytes());
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
