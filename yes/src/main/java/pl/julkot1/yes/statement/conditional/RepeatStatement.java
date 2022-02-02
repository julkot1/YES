package pl.julkot1.yes.statement.conditional;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RepeatStatement extends Statement {
    public RepeatStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if (!(astStatement.getArguments().size() == 2))throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        var counterType = astStatement.getArgument(0).getType();
        out.write(String.format("for(int i = 0; i <*((%s*)cr[0]); i++){",counterType.getCToken()).getBytes());
        DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(1)), out);
        out.write("}".getBytes());
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(0)), out);
    }
}
