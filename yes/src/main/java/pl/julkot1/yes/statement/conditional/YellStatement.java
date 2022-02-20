package pl.julkot1.yes.statement.conditional;

import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class YellStatement extends Statement {
    public YellStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if (!(astStatement.getArguments().size() == 2))throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
        astStatement.getArgument(0).setType(Type.BOOL);
        var arg1 = astStatement.getArgument(1);
        if(arg1 instanceof Array && arg1.getType().equals(Type.NULL)){
            arg1.setType(Type.STR);
        }
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        out.write("if(!*((unsigned char*)cr[0])){".getBytes());
        DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(1)), out);
        var type = astStatement.getArgument(1).getType();
        out.write((String.format("printf(\"%s: by YELL statement: line %d\\n\", *((%s *) cr[1]));", type.getCFormatSpecifier(), astStatement.getLine(),type.getCToken())).getBytes());
        out.write("exit(EXIT_FAILURE);}".getBytes());

    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(0)), out);
    }
}
