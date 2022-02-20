package pl.julkot1.yes.statement.array;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DelStatement extends Statement {
    public DelStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if (astStatement.getArguments().size() != 1)
            throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        out.write(String.format("for (int i = 0; i <%s; i++){free(gr[ptg -i]);}ptg-=%s;", arguments.get(0), arguments.get(0)).getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
