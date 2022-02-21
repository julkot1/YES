package pl.julkot1.yes.statement.custom;

import org.apache.commons.lang3.math.NumberUtils;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.generator.parser.StatementParser;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.StatementRegister;
import pl.julkot1.yes.statement.custom.interfaces.ArgumentCount;
import pl.julkot1.yes.statement.custom.interfaces.Interface;
import pl.julkot1.yes.statement.custom.interfaces.InterfaceRegister;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.util.StatementUtils;

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
        astStatement.setType(astStatement.getArgument(1).getType());
        var cs = new CustomStatement(this.astStatement, astStatement.getArgument(0).getToken(), "");
        StatementRegister.add(cs);
        var type = this.astStatement.getType();
        var cType = type.equals(Type.STR)?"char*":type.getCToken();
        out.write(String.format("%s %s(%s){", type.equals(Type.NULL)?"void":cType, astStatement.getArgument(0).getToken(), setArgs(cs)).getBytes());
        out.write(String.format("size_t pta = %d;", getPTA(cs)).getBytes());
        var s = (NestedStatement) cs.astStatement.getArgument(1);
        for (Argument argument : s.getStack()) {
            StatementParser.writeStatement((AstStatement) argument, out, true);
        }
        DefaultGenerators.writeArguments(s.getStack(), out);
        out.write("}".getBytes());
    }

    private int getPTA(CustomStatement cs) throws InvalidYesSyntaxException {
        if(InterfaceRegister.contains(cs.getToken())) {
            var anInterface = InterfaceRegister.get(cs.getToken()).get();
            return StatementUtils.argumentsSize(anInterface.getArgumentCounts());
        }
        return 0;
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return null;
    }
    private String setArgs(CustomStatement cs) throws InvalidYesSyntaxException {
        if(InterfaceRegister.contains(cs.getToken())){
            var anInterface = InterfaceRegister.get(cs.getToken()).get();
            StringBuilder argv = new StringBuilder();int count = 0;
            for (ArgumentCount argumentCount : anInterface.getArgumentCounts()) {
                for (int i = 0; i < argumentCount.getValue(); i++) {
                    var type = argumentCount.getType();
                    argv.append(type.equals(Type.STR)?"char *":type.getCToken()).append(" ar").append(count).append(",");
                    count++;
                }
            }
            return argv.substring(0, argv.toString().length()-1);
        }else return "";
    }
    @Override
    public void generate(FileOutputStream out, boolean writeOut, boolean __) throws IOException, InvalidYesSyntaxException {
        validArguments();
        applyPrefixes(out);
        writeArguments(out);
        write(out);
    }
}
