package pl.julkot1.yes.statement.io;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CallStatement extends Statement {
    public CallStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {

    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        StringBuilder args = new StringBuilder();
        var size = 0;
        for (int i = 0; i < this.astStatement.getArguments().size(); i++) {
            var argument = this.astStatement.getArgument(i);
            size += argument.getToken().length()+1;
            args.append("*((")
                    .append(argument.getType().getCToken())
                    .append("*)cr[").append(i).append("])");
            if(i+1 < this.astStatement.getArguments().size())args.append(",");
        }
        args.replace(args.length()-1, args.length()-1, "");
        out.write(("char buffer[strlen(*((char **)cr[0]))+"+size+"];").getBytes());
        out.write(String.format("sprintf(buffer, %s);", args).getBytes());
        out.write("system(buffer);".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
