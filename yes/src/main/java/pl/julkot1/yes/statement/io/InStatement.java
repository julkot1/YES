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
        setMustBeWritten(true);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if(this.astStatement.getType().equals(Type.NULL)) throw  new InvalidYesSyntaxException(astStatement.getLine(), "Type must be specified!");

    }

    @Override
    protected void setReturning() throws InvalidYesSyntaxException {
        setReturning(String.format("*((%s*)gr[ptg-1])", astStatement.getType().getCToken()));
    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        var type = this.astStatement.getType();
        out.write("{".getBytes());
        if(type.equals(Type.STR)) {
            out.write("char *buffer;".getBytes());
            out.write("scanf(\"%ms\", &buffer);".getBytes());
            out.write("gr[ptg] = strdup(buffer);ptg++;".getBytes());
        }
        else {
            out.write((type.getCToken() + " buffer;").getBytes());
            out.write(("scanf(\"" + type.getCFormatSpecifier() + "\", &buffer);").getBytes());
            out.write(String.format("gr[ptg]=malloc(sizeof(%s))*((%s *)gr[ptg]) = buffer;ptg++;", type.getCToken(), type.getCToken()).getBytes());
        }
        out.write("}".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return null;
    }
}
