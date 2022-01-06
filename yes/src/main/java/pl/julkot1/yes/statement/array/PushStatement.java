package pl.julkot1.yes.statement.array;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.Statement;

import java.io.FileOutputStream;
import java.io.IOException;

public class PushStatement extends Statement {
    public PushStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {

    }

    @Override
    protected void write(FileOutputStream out) throws IOException {

    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException {

    }
}
