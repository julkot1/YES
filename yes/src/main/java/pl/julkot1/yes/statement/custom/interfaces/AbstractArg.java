package pl.julkot1.yes.statement.custom.interfaces;

import org.apache.commons.lang3.math.NumberUtils;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public abstract class AbstractArg extends Statement {

    public AbstractArg(AstStatement astStatement) {
        super(astStatement);
    }


    public void validArg()throws InvalidYesSyntaxException{
        if(astStatement.getType().equals(Type.NULL))throw new InvalidYesSyntaxException(astStatement.getLine(),  "argument must have specified type!");
        for (Argument argument : astStatement.getArguments()) {
            if(argument.getType().equals(Type.NULL))throw new InvalidYesSyntaxException(astStatement.getLine(),  "invalid argument!");
            if(!NumberUtils.isDigits(argument.getToken()))throw new InvalidYesSyntaxException(astStatement.getLine(),  "invalid argument!");
        }
        initValidArg();
    }
    protected abstract void initValidArg()throws InvalidYesSyntaxException;


    @Override
    protected void validArguments() throws InvalidYesSyntaxException {}
    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {}
    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {return null;}

    public abstract int applyArg(List<ArgumentCount> argumentCounts);
}
