package pl.julkot1.yes.statement.other;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.StatementTokens;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class RtStatement extends Statement {
    public RtStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        if(!(astStatement.getParent() instanceof NestedStatement))
            throw new InvalidYesSyntaxException(astStatement.getLine(), astStatement.getToken()+" must be called inside nested statement");
        if(astStatement.getArguments().size() != 1)
            throw new InvalidArgumentsQuantity(astStatement.getLine(),  astStatement.getToken());
        if(astStatement.getParent()!=null){
            if(astStatement.getParent().getParent()!=null){
                if(!astStatement.getParent().getParent().getToken().equals(StatementTokens.STATEMENT_DEF.getToken())){
                    if(astStatement.getParent().getType()== Type.NULL||astStatement.getParent().getType()==null)
                        throw new InvalidYesSyntaxException(astStatement.getLine(), astStatement.getToken()+" can't be called inside Null type nested statement");
                }
            }else if(astStatement.getParent().getType()== Type.NULL||astStatement.getParent().getType()==null)
                throw new InvalidYesSyntaxException(astStatement.getLine(), astStatement.getToken()+" can't be called inside Null type nested statement");
        }else if(astStatement.getParent().getType()== Type.NULL||astStatement.getParent().getType()==null)
            throw new InvalidYesSyntaxException(astStatement.getLine(), astStatement.getToken()+" can't be called inside Null type nested statement");

        
    }



    @Override
    protected void write(FileOutputStream out) throws IOException {
        var type = astStatement.getArgument(0).getType().getCToken();
        var nIndex = ((NestedStatement)astStatement.getParent()).getNIndex();
        if(nIndex==-1)out.write(String.format("return %s;", arguments.get(0)).getBytes());
        else out.write(String.format("%s n%d = %s;", type, nIndex, arguments.get(0)).getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
