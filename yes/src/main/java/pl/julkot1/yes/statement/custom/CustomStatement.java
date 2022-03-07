package pl.julkot1.yes.statement.custom;

import lombok.Getter;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.Statement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CustomStatement extends Statement {
    @Getter
    private final String token;
    @Getter
    private final String namespace;
    public CustomStatement(AstStatement astStatement, String token, String namespace) {
        super(astStatement);
        this.token = token;
        this.namespace = namespace==null?"_GLOBAL":namespace;
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {

    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return null;
    }
}
