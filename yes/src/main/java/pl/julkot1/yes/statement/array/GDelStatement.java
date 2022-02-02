package pl.julkot1.yes.statement.array;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;

import java.io.FileOutputStream;
import java.io.IOException;

public class GDelStatement extends Statement {
    public GDelStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if (astStatement.getArguments().size() != 1)
            throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        out.write("if(*((unsigned long*)cr[0]) <= ptg){ptg-=*((unsigned long*)cr[0]); for (int i = 0; i < *((unsigned long *)cr[0]); i++)free(gr[ptg -i]);}".getBytes());
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
