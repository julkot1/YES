package pl.julkot1.yes.statement.array;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Alloc extends Statement {
    public Alloc(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if (astStatement.getArguments().size() != 1)
            throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
    }
    @Override
    protected void write(FileOutputStream out) throws IOException {
        var type = astStatement.getArgument(0).getType().getCToken();
        if(astStatement.getType().equals(Type.STR)){
            out.write("gr[ptg] = malloc(sizeof(char*));".getBytes());
            out.write(String.format("*((char **)gr[ptg])=(char*)malloc((*((%s*)cr[0])+1)*sizeof(char));", type).getBytes());
            out.write("ptg++;".getBytes());
        }else{
            out.write(String.format("gr[ptg] = malloc(*((%s*)cr[0]));", type).getBytes());
            out.write("ptg++;".getBytes());
        }

    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
