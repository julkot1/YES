package pl.julkot1.yes.statement.custom;

import lombok.Getter;
import lombok.Setter;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.custom.interfaces.Interface;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CustomStatement extends Statement {
    @Getter
    private final String token;
    @Getter
    private final String nameSpace;
    @Getter @Setter
    private Interface anInterface = null;
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
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return null;
    }
}
