package pl.julkot1.yes.statement.io;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;

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
        out.write(String.format("xr[ptx] = malloc(sizeof(%s));", type.getCToken()).getBytes());
        if (type == Type.STR)
            out.write("scanf(\"%ms\", &buffer);".getBytes());
        else
            out.write(("scanf(\""+type.getCFormatSpecifier()+"\", &buffer);").getBytes());
        out.write(String.format("*((%s *)xr[ptx]) = buffer;ptx++;",type.getCToken()).getBytes());
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {

    }
}
