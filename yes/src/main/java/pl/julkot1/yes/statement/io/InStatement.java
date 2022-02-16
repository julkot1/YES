package pl.julkot1.yes.statement.io;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class InStatement extends Statement {
    public InStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if(this.astStatement.getType().equals(Type.NULL)) throw  new InvalidYesSyntaxException(astStatement.getLine(), "Type must be specified!");

    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        var type = this.astStatement.getType();
        out.write((type.getCToken()+" buffer;").getBytes());
        if (type == Type.STR)
            out.write("scanf(\"%ms\", &buffer);".getBytes());
        else
            out.write(("scanf(\""+type.getCFormatSpecifier()+"\", &buffer);").getBytes());
        out.write(String.format("*((%s *)xr[2]) = buffer;",type.getCToken()).getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return null;
    }
}
