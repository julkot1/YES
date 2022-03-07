package pl.julkot1.yes.statement.custom;

import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.StatementRegister;
import pl.julkot1.yes.statement.custom.interfaces.ArgumentCount;
import pl.julkot1.yes.statement.custom.interfaces.InterfaceRegister;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CustomStatementImpl extends Statement {

    public CustomStatementImpl(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {

        var statement = StatementRegister.get(astStatement);
        var anInterfaceO = InterfaceRegister.get(statement.getToken(), statement.getNamespace());
        if(anInterfaceO.isPresent()){
            var args = astStatement.getArguments();
            var anInterface = anInterfaceO.get();
            if(args.size()!=anInterface.getArgs())throw new InvalidArgumentsQuantity(astStatement.getLine(),  astStatement.getToken());
            var index = 0;
            for (ArgumentCount argumentCount : anInterface.getArgumentCounts()) {
                var count = argumentCount.getValue();
                var type = argumentCount.getType();
                for (Argument argument : args.subList(index, count+index)) {
                    if(argument instanceof Array){
                        if(argument.getType().equals(Type.NULL)){
                            argument.setType(type);
                        }
                    }
                    if(type!=argument.getType())
                        throw new TypeException(astStatement.getLine(),argument.getToken(), "expected "+type.getYesToken()+" type!");
                }
                index+=count;
            }
        }
    }

    @Override
    protected void setReturning() throws InvalidYesSyntaxException {
        var s = StatementRegister.get(astStatement);
        StringBuilder a= new StringBuilder();
        for (String argument : arguments) {
            a.append(argument.contains("(char*)")?"strdup"+argument:argument).append(",");
        }
        setReturning(s.getNamespace()+s.getToken()+"("+a.substring(0,a.length()-1)+")");
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {

        var statement = StatementRegister.get(astStatement);
        if(!statement.astStatement.getType().equals(Type.NULL)){
            if(statement.astStatement.getType().equals(Type.STR))
                out.write("xr[0]=strdup(".getBytes());
            else
                out.write(String.format("*((%s *)xr[0])=", statement.astStatement.getType().getCToken()).getBytes());
        }
        out.write(getReturning().getBytes());
        if(statement.astStatement.getType().equals(Type.STR))
            out.write(")".getBytes());
        out.write(";".getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(astStatement.getArguments(), out);
    }
}
