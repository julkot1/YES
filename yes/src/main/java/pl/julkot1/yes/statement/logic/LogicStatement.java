package pl.julkot1.yes.statement.logic;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.DefaultTypes;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;


public class LogicStatement extends Statement {
    protected String operator;

    public LogicStatement(AstStatement astStatement, String operator) {
        super(astStatement);
        this.operator = operator;
        astStatement.setType(Type.BOOL);
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
        var resultType = DefaultTypes.getMathType(argumentsTypes);

        out.write(String.format("xr[ptx] = malloc(sizeof(%s));", resultType.getCToken()).getBytes());
        out.write(String.format("*((%s *)xr[ptx]) = ", resultType.getCToken()).getBytes());
        out.write(String.format("*((%s*)cr[0]) ", argumentsTypes.get(0).getCToken()).getBytes());
        out.write(this.operator.getBytes());
        out.write(String.format("*((%s*)cr[1]);", argumentsTypes.get(0).getCToken()).getBytes());
        out.write("ptx++;".getBytes());
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException, InvalidYesSyntaxException {
        DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
