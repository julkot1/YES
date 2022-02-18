package pl.julkot1.yes.statement.str;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CharAtStatement extends Statement {
    public CharAtStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if (astStatement.getArguments().size() != 2)
            throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());

        if (!astStatement.getArgument(0).getType().equals(Type.STR))
            throw new TypeException(astStatement.getLine(), astStatement.getToken(), "argument must be"+Type.STR.getYesToken());
        
    }

    @Override
    protected void setReturning() throws InvalidYesSyntaxException {
        setReturning(arguments.get(0)+"["+arguments.get(1)+"]");
    }

    @Override
    protected void write(FileOutputStream out) throws IOException {

        out.write("*((char *)xr[0]) =".getBytes());
        out.write(getReturning().getBytes());
        out.write(";".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
