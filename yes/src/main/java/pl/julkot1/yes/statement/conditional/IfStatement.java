package pl.julkot1.yes.statement.conditional;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.util.ArgumentsValidation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class IfStatement extends Statement {
    public IfStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        int quantity = isSingleStatement()?
                3:
                astStatement.getType().equals(Type.NULL)?1:2;
        var validator = ArgumentsValidation.builder()
                .quantity(quantity).minQuantity().maxQuantity(isSingleStatement()?3:2)
                .build();
        validator.check(astStatement.getArguments(), astStatement);

    }
    private boolean isSingleStatement(){
        if(astStatement.getParent() instanceof NestedStatement){
            return ((NestedStatement) astStatement.getParent()).isSingleStatement();
        }
        return false;
    }
    @Override
    protected void setReturning() throws InvalidYesSyntaxException {
        if(!astStatement.getType().equals(Type.NULL)){
            if(isSingleStatement())
                setReturning("("+arguments.get(0)+"?"+arguments.get(1)+":"+arguments.get(2)+")");
            else
                setReturning("(*((unsigned char *)rx)?"+arguments.get(0)+":"+arguments.get(1)+")");
        }
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        if(!astStatement.getType().equals(Type.NULL)){
            var type = astStatement.getType().getCToken();
            out.write(String.format("*((%s)rx)= %s;", type,  getReturning()).getBytes());
            out.write(";".getBytes());
        }else{
            out.write("if(*((unsigned char*)rx)){".getBytes());
            DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(0)), out);
            out.write("}".getBytes());
            if (astStatement.getArguments().size()==2){
                out.write("else{".getBytes());
                DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(1 )), out);
                out.write("}".getBytes());
            }
        }

    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        if(!astStatement.getType().equals(Type.NULL))return DefaultGenerators.writeArguments(astStatement.getArguments(), out);
        return null;
    }
}
