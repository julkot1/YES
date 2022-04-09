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
import java.util.List;


public class LogicStatement extends Statement {
    protected String operator;

    public LogicStatement(AstStatement astStatement, String operator) {
        super(astStatement);
        this.operator = operator;
        this.astStatement.setType(Type.INT);
    }

    @Override
    protected void validArguments(){
        boolean quantity = this.astStatement.getArguments().size() == 2;
        if(!quantity)throw new InvalidArgumentsQuantity(this.astStatement.getLine(), this.astStatement.getToken());

    }

    @Override
    protected void setReturning() {
        setReturning("("+arguments.get(0)+")"+this.operator+"("+arguments.get(1)+")");
    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        var resultType = astStatement.getType().getCToken();
        out.write(String.format("*((%s *)rx) = ", resultType).getBytes());
        out.write(getReturning().getBytes());
        out.write(";".getBytes());

    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
