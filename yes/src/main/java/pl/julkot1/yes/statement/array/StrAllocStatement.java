package pl.julkot1.yes.statement.array;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.util.ArgumentsValidation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class StrAllocStatement extends Statement {
    public StrAllocStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        var validator = ArgumentsValidation.builder()
                .quantity(1)
                .enableTypeCheck()
                .argumentType(0, Type.SIZE)
                .build();
        validator.check(astStatement.getArguments(), astStatement);
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        out.write(String.format("gr[ptg] = malloc(sizeof(char)*%s);ptg++;", arguments.get(0)).getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return arguments = DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
