package pl.julkot1.yes.statement.array;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.util.ArgumentsValidation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PushStatement extends Statement {
    public PushStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        var validator = ArgumentsValidation.builder()
                .quantity(1)
                .build();
        validator.check(astStatement.getArguments(), astStatement);

    }
    @Override
    protected void write(FileOutputStream out) throws IOException {
        var type = astStatement.getArgument(0).getType().getCToken();
        if(astStatement.getArgument(0).getType().equals(Type.STR)){
            out.write(String.format("gr[ptg] = strdup(%s);", arguments.get(0)).getBytes());
        }else{
            out.write(String.format("gr[ptg] = malloc(sizeof(%s));", type).getBytes());
            out.write(String.format("*((%s*)gr[ptg]) = %s;", type, arguments.get(0)).getBytes());
        }
        out.write("ptg++;".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return arguments = DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
