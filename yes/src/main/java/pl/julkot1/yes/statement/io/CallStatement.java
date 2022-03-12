package pl.julkot1.yes.statement.io;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.util.ArgumentsValidation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CallStatement extends Statement {
    public CallStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        var validator = ArgumentsValidation.builder()
                .quantity(1).enableTypeCheck()
                .argumentType(0, Type.STR)
                .build();
        validator.check(astStatement.getArguments(), astStatement);
    }

    @Override
    protected void setReturning() throws InvalidYesSyntaxException {
        setReturning("system("+arguments.get(0)+")");
    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        out.write("gr[ptg]=malloc(sizeof(int));".getBytes());
        out.write("*((int *)gr[ptg])=".getBytes());
        out.write(getReturning().getBytes());
        out.write(";ptg++;".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
