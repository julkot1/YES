package pl.julkot1.yes.statement.io;

import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.DefaultTypes;

import java.io.FileOutputStream;
import java.io.IOException;

public class EchoStatement extends Statement {
    public EchoStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {

    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        StringBuilder args = new StringBuilder();
        for (int i = 0; i < this.astStatement.getArguments().size(); i++) {
            var argument = this.astStatement.getArgument(i);
            args.append("*((")
                    .append(argument.getType().getCToken())
                    .append("*)cr[").append(i).append("]),");
        }
        out.write("char buffer[strlen(*((char **)cr[0]))];".getBytes());
        out.write(String.format("sprintf(buffer, %s);", args).getBytes());
        out.write("printf(\"%s\", buffer)".getBytes());
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
