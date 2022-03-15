package pl.julkot1.yes.statement.array;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.ErrorCodes;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.util.ArgumentsValidation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PopStatement extends Statement {
    public PopStatement(AstStatement astStatement) {
        super(astStatement);
    }
    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        var validator = ArgumentsValidation.builder()
                .quantity(0)
                .build();
        if(this.astStatement.getType().equals(Type.NULL))
            throw new InvalidYesSyntaxException(this.astStatement, ErrorCodes.TYPE_REQUIRED);
        validator.check(astStatement.getArguments(), astStatement);

    }


    @Override
    protected void setReturning() throws InvalidYesSyntaxException {
        setReturning(String.format("*((%s *)pop(sizeof(%s)))", astStatement.getType().getCToken(), astStatement.getType().getCToken()));

    }

    @Override
    protected void write(FileOutputStream out) throws IOException {;
        if(astStatement.getType().equals(Type.STR))
            out.write("rx=".getBytes());
        else
            out.write(String.format("*(%s *)rx=", astStatement.getType().getCToken()).getBytes());
        out.write(getReturning().getBytes());
        out.write(";".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return  new ArrayList<>();
    }
}
