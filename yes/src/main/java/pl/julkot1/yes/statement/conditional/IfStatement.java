package pl.julkot1.yes.statement.conditional;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class IfStatement extends Statement {
    public IfStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if (!(astStatement.getArguments().size() == 2 || astStatement.getArguments().size() == 3))throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
        astStatement.getArgument(0).setType(Type.BOOL);
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        out.write("if(*((unsigned char*)cr[0])){".getBytes());
        DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(1)), out);
        var type = astStatement.getArgument(1).getType().getCToken();
        out.write(String.format("}").getBytes());
        if (astStatement.getArguments().size()==3){
            out.write("else{".getBytes());
            DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(2)), out);
            var type2 = astStatement.getArgument(2).getType().getCToken();
            out.write(String.format("}").getBytes());
        }
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(0)), out);
    }
}
