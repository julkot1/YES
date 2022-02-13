package pl.julkot1.yes.statement.custom;

import lombok.Getter;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.generator.parser.StatementParser;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.StatementRegister;
import pl.julkot1.yes.statement.custom.interfaces.ArgumentCount;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.stream.Collectors;

public class CustomStatementImpl extends Statement {

    public CustomStatementImpl(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {

        var statement = StatementRegister.get(astStatement.getToken());
        var anInterface = statement.getAnInterface();
        if(anInterface!=null){
            var args = astStatement.getArguments();

            if(args.size()!=anInterface.getArgs())throw new InvalidArgumentsQuantity(astStatement.getLine(),  astStatement.getToken());
            var index = 0;
            for (ArgumentCount argumentCount : anInterface.getArgumentCounts()) {
                var count = argumentCount.getValue();
                var type = argumentCount.getType();
                for (Argument argument : args.subList(index, count+index)) {
                    if(type!=argument.getType())
                        throw new TypeException(astStatement.getLine(),argument.getToken(), "expected "+type.getYesToken()+" type!");
                }
                index+=count;
            }
        }
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {

        var s = (NestedStatement) StatementRegister.get(astStatement.getToken()).astStatement.getArgument(1);
        for (Argument argument : s.getStack()) {
            StatementParser.writeStatement((AstStatement) argument, out);
        }
        DefaultGenerators.writeArguments(s.getStack(), out);
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        DefaultGenerators.writeArguments(astStatement.getArguments(), out);

    }
}
