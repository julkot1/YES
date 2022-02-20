package pl.julkot1.yes.statement.other;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
import pl.julkot1.yes.statement.Statement;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Syscall extends Statement {
    public Syscall(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if(astStatement.getArguments().size()==0)throw  new InvalidArgumentsQuantity(astStatement.getLine(), astStatement.getToken());

    }

    @Override
    protected void setReturning() throws InvalidYesSyntaxException {
        StringBuilder args = new StringBuilder();
        for (String argument : arguments) {
            args.append(argument).append(",");
        }
        setReturning("syscall("+args.substring(0, args.length()-1)+")");
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        out.write("*((int *)xr[0])=".getBytes());
        out.write(getReturning().getBytes());
        out.write(";".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
