package pl.julkot1.yes.statement.custom;

import lombok.Getter;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.Statement;

import java.io.FileOutputStream;
import java.io.IOException;

public class CustomStatement extends Statement {
    @Getter
    private final String token;
    @Getter
    private final String nameSpace;
    public CustomStatement(AstStatement astStatement, String token, String nameSpace) {
        super(astStatement);
        this.token = token;
        this.nameSpace = nameSpace;
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        //TODO _INTERFACE
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
    }
}
