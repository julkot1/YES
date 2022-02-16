package pl.julkot1.yes.statement.custom;

import org.apache.commons.lang3.math.NumberUtils;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.StatementRegister;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static pl.julkot1.yes.generator.GeneratorConstance.STATEMENT_NAME_PATTERN;

public class StatementDeclaration extends Statement {
    public StatementDeclaration(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if(!(astStatement.getArguments().size() >= 2 && astStatement.getArguments().size() <= 3))
            throw new InvalidArgumentsQuantity(astStatement.getLine(),  astStatement.getToken());
        if(astStatement.getArgument(0).getType()!= Type.NULL || !astStatement.getArgument(0).getToken().matches(STATEMENT_NAME_PATTERN))
            throw new InvalidYesSyntaxException(astStatement.getLine(), astStatement.getArgument(0).getToken()+" statement invalid name!");
        if(!(astStatement.getArgument(1) instanceof NestedStatement))
            throw new InvalidYesSyntaxException(astStatement.getLine(), astStatement.getArgument(0).getToken()+": body missing!");
        if(astStatement.getArguments().size()==3){
            if(!NumberUtils.isDigits(astStatement.getArgument(2).getToken()))
                throw new InvalidYesSyntaxException(astStatement.getLine(), astStatement.getArgument(0).getToken()+": invalid array size!");
        }
        if(astStatement.getParent()!=null)
            throw new InvalidYesSyntaxException(astStatement.getLine(), astStatement.getArgument(0).getToken()+": must be defined in global scope!");
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        StatementRegister.add(new CustomStatement(this.astStatement, astStatement.getArgument(0).getToken(), ""));
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return null;
    }

    @Override
    public void generate(FileOutputStream out, boolean writeOut) throws IOException, InvalidYesSyntaxException {
        validArguments();
        applyPrefixes(out);
        writeArguments(out);
        write(out);
    }
}
