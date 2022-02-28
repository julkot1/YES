package pl.julkot1.yes.statement.str;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.exception.ErrorCodes;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.lexer.tokens.SyntaxTokens;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.util.ArgumentsValidation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FormatStatement extends Statement {
    public FormatStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        var validator = ArgumentsValidation.builder()
                .quantity(2)
                .minQuantity()
                .enableTypeCheck()
                .enableCustomCheck()
                .argumentType(0, Type.STR)
                .customPredicate(0, argument ->
                        argument instanceof Value ? ErrorCodes.SUCCESS : ErrorCodes.STR_TEMPLATE
                )
                .build();
        validator.check(astStatement.getArguments(), astStatement);
    }

    @Override
    protected void setReturning() throws InvalidYesSyntaxException {
        setReturning("strdup(buffer)");
    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        out.write("{".getBytes());
        out.write("char *buffer;".getBytes());
        out.write(String.format("asprintf(&buffer, %s);",String.join(", ", arguments)).getBytes());
        out.write("xr[0] = strdup(buffer);".getBytes());
        out.write("}".getBytes());
    }


    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
