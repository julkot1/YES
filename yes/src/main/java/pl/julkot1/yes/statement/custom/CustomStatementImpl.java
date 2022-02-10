package pl.julkot1.yes.statement.custom;

import lombok.Getter;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.generator.parser.StatementParser;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.StatementRegister;

import java.io.FileOutputStream;
import java.io.IOException;

public class CustomStatementImpl extends Statement {

    public CustomStatementImpl(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        //TODO _INTERFACE
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {

        NestedStatement s = (NestedStatement) StatementRegister.get(astStatement.getToken()).astStatement.getArgument(1);
        for (Argument argument : s.getStack()) {
            StatementParser.writeStatement((AstStatement) argument, out);
        }
        DefaultGenerators.writeArguments(s.getStack(), out);
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        DefaultGenerators.writeArguments(astStatement.getArguments(), out);

    }
}
