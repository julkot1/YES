package pl.julkot1.yes.statement.array;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.Statement;

import java.io.FileOutputStream;
import java.io.IOException;

public class ReplaceStatement extends PushStatement {
    public ReplaceStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        var type = astStatement.getArgument(0).getType().getCToken();
        out.write(String.format("gr[ptg-1] = malloc(sizeof(%s));", type).getBytes());
        out.write(String.format("*((%s*)gr[ptg-1]) = *((%s*)cr[0]);", type, type).getBytes());
    }
}
