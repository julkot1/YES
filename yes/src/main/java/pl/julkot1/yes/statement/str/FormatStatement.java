package pl.julkot1.yes.statement.str;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.lexer.tokens.SyntaxTokens;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FormatStatement extends Statement {
    public FormatStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if (astStatement.getArguments().size() < 2)
            throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
        var template = astStatement.getArgument(0);
        if(!(template.getType().equals(Type.STR)&&template instanceof Value))
            throw new InvalidYesSyntaxException(astStatement.getLine(), "template must be const Str");
        if (!astStatement.getArgument(0).getType().equals(Type.STR))
            throw new TypeException(astStatement.getLine(), astStatement.getToken(), "argument must be"+Type.STR.getYesToken());

    }



    @Override
    protected void write(FileOutputStream out) throws IOException {
        out.write("{".getBytes());
        out.write("char *buffer;".getBytes());
        out.write(String.format("asprintf(&buffer, %s);",String.join(", ", arguments)).getBytes());
        out.write("xr[0] = strdup(buffer);".getBytes());
        out.write("}".getBytes());
    }


    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
