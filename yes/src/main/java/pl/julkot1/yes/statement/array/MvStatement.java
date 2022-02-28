package pl.julkot1.yes.statement.array;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.ErrorCodes;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.lexer.tokens.SpecialTypeTokens;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.util.ArgumentsValidation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MvStatement extends Statement {

    public MvStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        var validator = ArgumentsValidation.builder()
                .quantity(2)
                .enableCustomCheck().enableTypeCheck()
                .argumentRequiredType(1)
                .customPredicate(0, argument ->
                        argument.getToken().equals(SpecialTypeTokens.XR.getToken())?ErrorCodes.SUCCESS:ErrorCodes.MV_INVALID_ARRAY)
                .build();
        validator.check(astStatement.getArguments(), astStatement);
        var type = astStatement.getArgument(1).getType();
        astStatement.getArgument(0).setType(type);
    }
    @Override
    protected void write(FileOutputStream out) throws IOException {
        if(astStatement.getArguments().get(1).getType().equals(Type.STR)){
            var arg0 = arguments.get(0).substring(8, arguments.get(0).length()-1);
            out.write(String.format("*((char**)%s) = strdup(%s);", arg0, arguments.get(1)).getBytes());
        }
        else out.write(String.format("%s = %s;", arguments.get(0), arguments.get(1)).getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return arguments = DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
