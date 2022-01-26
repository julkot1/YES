package pl.julkot1.yes.statement.array;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;

public class PushStatement extends Statement {
    public PushStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if (astStatement.getArguments().size() != 1)
            throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
        if (astStatement.getArgument(0).getType().equals(Type.NULL))
            throw new TypeException(astStatement.getLine(), astStatement.getToken(), "type must be specified");
    }
    @Override
    protected void write(FileOutputStream out) throws IOException {
        var type = astStatement.getArgument(0).getType().getCToken();
        out.write(String.format("gr[ptg] = malloc(sizeof(%s));", type).getBytes());
        out.write(String.format("*((%s*)gr[ptg]) = *((%s*)cr[0]);", type, type).getBytes());
        out.write("ptg++;".getBytes());
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
