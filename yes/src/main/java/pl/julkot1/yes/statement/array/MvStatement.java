package pl.julkot1.yes.statement.array;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.lexer.tokens.SpecialTypeTokens;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MvStatement extends Statement {

    public MvStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if (astStatement.getArguments().size() != 2)
            throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
        if(!astStatement.getArgument(0).getToken().equals(SpecialTypeTokens.XR.getToken()))
            throw new InvalidYesSyntaxException(astStatement.getArgument(0).getLine(),
                    "expected "+SpecialTypeTokens.XR.getToken()+ " instead of "+astStatement.getArgument(0).getToken());
        if (astStatement.getArgument(1).getType().equals(Type.NULL))
            throw new TypeException(astStatement.getLine(), astStatement.getToken(), "type must be specified");
        if(!astStatement.getArgument(0).getType().equals(Type.NULL)){
            if(!astStatement.getArgument(0).getType().equals(astStatement.getArgument(1).getType())){
                throw new TypeException(astStatement.getLine(), astStatement.getToken(), "invalid types");
            }
        }
        var type = astStatement.getArgument(1).getType();
        astStatement.getArgument(0).setType(type);
    }
    @Override
    protected void write(FileOutputStream out) throws IOException {
        if(astStatement.getArguments().get(1).getType().equals(Type.STR)){
            var arg0 = arguments.get(0).substring(8, arguments.get(0).length()-1);
            out.write(String.format("%s = strdup(%s);", arg0, arguments.get(1)).getBytes());
        }
        else out.write(String.format("%s = %s;", arguments.get(0), arguments.get(1)).getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return arguments = DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
