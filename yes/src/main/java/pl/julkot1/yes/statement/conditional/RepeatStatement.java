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

public class RepeatStatement extends Statement {
    public RepeatStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if (!(astStatement.getArguments().size() == 2))throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
        var arg0 = astStatement.getArgument(0);
        if(arg0 instanceof Array && arg0.getType().equals(Type.NULL)){
            arg0.setType(Type.INT);
        }
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        var counterType = astStatement.getArgument(0).getType();
        out.write(String.format("for(size_t i = 0; i <%s; i++){*((size_t*)xr[1])=i;",arguments.get(0)).getBytes());
        DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(1)), out);
        out.write("}".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(0)), out);
    }
}
