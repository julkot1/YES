package pl.julkot1.yes.statement.str;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.util.ArgumentsValidation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CharAtStatement extends Statement {
    public CharAtStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        var validator = ArgumentsValidation.builder()
                .quantity(2)
                .enableTypeCheck()
                .argumentType(0, Type.STR)
                .argumentType(1, Type.INT)
                .build();
        validator.check(astStatement.getArguments(), astStatement);
    }

    @Override
    protected void setReturning() throws InvalidYesSyntaxException {
        setReturning(arguments.get(0)+"["+arguments.get(1)+"]");
    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        out.write("*((char *)rx) =".getBytes());
        out.write(getReturning().getBytes());
        out.write(";".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
