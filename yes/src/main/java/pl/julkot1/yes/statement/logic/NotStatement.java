package pl.julkot1.yes.statement.logic;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.DefaultTypes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class NotStatement extends Statement {

    public NotStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments(){
        boolean quantity = this.astStatement.getArguments().size() == 1;
        if(!quantity)throw new InvalidArgumentsQuantity(this.astStatement.getLine(), this.astStatement.getToken());
    }
    @Override
    protected void setReturning() {
        setReturning("!("+arguments.get(0)+")");
    }
    @Override
    protected void write(FileOutputStream out) throws IOException {
        var argumentsTypes = DefaultTypes.argumentsToTypesList(this.astStatement.getArguments());
        var resultType = DefaultTypes.getMathType(argumentsTypes);
        out.write("*((unsigned char *)rx) = ".getBytes());
        out.write(getReturning().getBytes());
        out.write(";".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
