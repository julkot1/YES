package pl.julkot1.yes.statement.other;

import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.StatementTokens;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MatchStatement extends Statement {
    public MatchStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if(astStatement.getArguments().size()!=2)
            throw new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());
        var arg1 = astStatement.getArgument(1);
        if(!(arg1 instanceof NestedStatement))
            throw new InvalidYesSyntaxException(arg1.getLine(), "2nd argument must be a nested statement");
        if(arg1.getType().equals(Type.NULL))
            throw new TypeException(arg1.getLine(), astStatement.getToken(), "type of nested statement must be declared");
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        var arg1 = (NestedStatement) astStatement.getArgument(1);
        var arg0 =  astStatement.getArgument(0);
        var first = true;
        for (Argument argument : arg1.getStack()) {
            var arg = (AstStatement) argument;
            if(!(arg.getToken().equals(StatementTokens.AS.getToken()) || arg.getToken().equals(StatementTokens.DO.getToken())))
                throw new InvalidYesSyntaxException(arg.getLine(), "MATCH nested statement must contain only AS and DO");
            var as = arg.getToken().equals(StatementTokens.AS.getToken())?
                    new AsStatement(arg, arg1.getType(), arg0.getType(), first, arguments.get(0)):
                    new DoStatement(arg, arg0.getType(), first, arguments.get(0));
            as.generate(out,true, false);
            first = false;
        }
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(0)), out);
    }
}
