package pl.julkot1.yes.statement.conditional;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.util.ArgumentsValidation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RepeatStatement extends Statement {
    public RepeatStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        var validator = ArgumentsValidation.builder()
                .quantity(2).enableTypeCheck().enableInstanceCheck()
                .argumentType(0, Type.INT)
                .argumentInstance(1, NestedStatement.class)
                .build();
        validator.check(astStatement.getArguments(), astStatement);
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        var counterType = astStatement.getArgument(0).getType();
        out.write(String.format("for(size_t i = 0; i <%s; i++){*((size_t*)xr[1])=i;",arguments.get(0)).getBytes());
        DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(1)), out);
        out.write("}".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(0)), out);
    }
}
