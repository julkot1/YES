package pl.julkot1.yes.statement.custom.interfaces;

import lombok.Getter;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.StatementRegister;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static pl.julkot1.yes.generator.GeneratorConstance.STATEMENT_NAME_PATTERN;

public class Interface extends Statement {
    public Interface(AstStatement astStatement) {
        super(astStatement);
        argumentCounts = new ArrayList<>();
    }
    @Getter
    private final List<ArgumentCount> argumentCounts;
    @Getter
    private int args;

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        var args =  astStatement.getArguments();
        if(!(args.size() >= 2))
            throw new InvalidArgumentsQuantity(astStatement.getLine(),  astStatement.getToken());
        if(args.get(0).getType()!= Type.NULL || !args.get(0).getToken().matches(STATEMENT_NAME_PATTERN))
            throw new InvalidYesSyntaxException(astStatement.getLine(), args.get(0).getToken()+" statement invalid name!");
        if(astStatement.getParent()!=null)
            throw new InvalidYesSyntaxException(astStatement.getLine(), args.get(0).getToken()+": must be defined in global scope!");
        if(!StatementRegister.contains(args.get(0).getToken()))
            throw new InvalidYesSyntaxException(astStatement.getLine(), "statement "+args.get(0).getToken()+" is not defined!");
        if(StatementRegister.get(args.get(0).getToken()).getAnInterface() != null)
            throw new InvalidYesSyntaxException(astStatement.getLine(), "statement "+args.get(0).getToken()+" has already had an interface!");

        for (Argument argument : args.subList(1, args.size())) {
            if(!(argument instanceof NestedStatement))
                throw new InvalidYesSyntaxException(astStatement.getLine(), argument.getToken()+ " invalid argument!");
            checkArgument((NestedStatement) argument);
        }
    }
    private void checkArgument(NestedStatement statement) throws InvalidYesSyntaxException {
        if(statement.getStack().size() != 1)
            throw new InvalidYesSyntaxException(astStatement.getLine(), statement.getToken()+ " invalid argument!");
        if(!InterfaceStatements.contains(statement.getStack().get(0).getToken()))
            throw new InvalidYesSyntaxException(astStatement.getLine(), statement.getToken()+ " invalid argument!");
        try {
            var arg =(Arg) InterfaceStatements.getByToken(statement.getStack().get(0).getToken()).get().getClazz().getConstructors()[0].newInstance(statement.getStack().get(0));
            arg.validArg();
            args += arg.applyArg( argumentCounts);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchElementException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        StatementRegister.get(astStatement.getArgument(0).getToken()).setAnInterface(this);
    }

    @Override
    protected void writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {

    }

    @Override
    public void generate(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        validArguments();
        applyPrefixes(out);
        writeArguments(out);
        write(out);
    }
}
