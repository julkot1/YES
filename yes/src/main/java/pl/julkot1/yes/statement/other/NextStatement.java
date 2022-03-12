package pl.julkot1.yes.statement.other;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.StatementTokens;
import pl.julkot1.yes.util.StatementUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class NextStatement extends Statement {
    public NextStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if(astStatement.getArguments().size()!=0)
            throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
        if(!StatementUtils.isStatementIn(this.astStatement, StatementTokens.REPEAT.getToken()))
            throw new InvalidYesSyntaxException(this.astStatement.getLine(), "statement must be inside REPEAT");
    }



    @Override
    protected void write(FileOutputStream out) throws IOException {
        out.write("if(*((char *)rx))continue;".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
