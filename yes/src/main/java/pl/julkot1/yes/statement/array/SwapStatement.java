package pl.julkot1.yes.statement.array;

import com.sun.jdi.InvalidTypeException;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;

import java.io.FileOutputStream;
import java.io.IOException;

public class SwapStatement extends Statement {
    public SwapStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if (astStatement.getArguments().size() != 2)
            throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
        if(!astStatement.getArgument(0).getType().equals(astStatement.getArgument(1).getType()))
            throw new TypeException(astStatement.getLine(), astStatement.getToken(), "arguments must have the same type");
        for(Argument argument : astStatement.getArguments()){
            if(!(argument instanceof Array))throw  new InvalidYesSyntaxException(astStatement.getLine(), argument.getToken()+" must be an array");
        }
    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        var type = astStatement.getArgument(0).getType().getCToken();
        out.write(String.format("%s tempA = *((%s*)cr[0]);", type, type).getBytes());
        out.write(String.format("%s tempB = *((%s*)cr[1]);", type, type).getBytes());
        out.write(String.format("*((%s*)cr[0]) = tempB;",type).getBytes());
        out.write(String.format("*((%s*)cr[1]) = tempA;", type).getBytes());
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
