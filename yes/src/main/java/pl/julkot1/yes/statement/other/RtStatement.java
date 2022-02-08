package pl.julkot1.yes.statement.other;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RtStatement extends Statement {
    public RtStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if(!(astStatement.getParent() instanceof NestedStatement))
            throw new InvalidYesSyntaxException(astStatement.getLine(), astStatement.getToken()+" must be called inside nested statement");
        if(astStatement.getArguments().size() != 1)
            throw new InvalidArgumentsQuantity(astStatement.getLine(),  astStatement.getToken());
        if(astStatement.getParent().getType()== Type.NULL||astStatement.getParent().getType()==null)
            throw new InvalidYesSyntaxException(astStatement.getLine(), astStatement.getToken()+" can't be called inside Null type nested statement");
        if(astStatement.getParent().getType() !=  astStatement.getArguments().get(0).getType())
            throw new InvalidYesSyntaxException(astStatement.getLine(), astStatement.getToken()+": returning argument must be the same type as nested statement");
    }

    @Override
    protected void write(FileOutputStream out) throws IOException {
        var type = astStatement.getArgument(0).getType().getCToken();
        out.write(String.format("*(xr + ptx) = malloc(sizeof(%s));", type).getBytes());
        out.write(String.format("*((%s*)xr[ptx]) = *((%s*)cr[0]);ptx++;break;", type, type).getBytes());
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
