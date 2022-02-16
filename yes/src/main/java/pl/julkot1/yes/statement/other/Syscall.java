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
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        StringBuilder args = new StringBuilder();
        var size = 0;
        for (int i = 0; i < this.astStatement.getArguments().size(); i++) {
            var argument = this.astStatement.getArgument(i);
            size += argument.getToken().length()+1;
            if(argument.getPrefixes().contains(PrefixTokens.REFERENCE)){
                args.append("cr[").append(i).append("]");
            }else {
                args.append("*((")
                        .append(argument.getType().getCToken())
                        .append("*)cr[").append(i).append("])");
            }
            if(i+1 < this.astStatement.getArguments().size())args.append(",");
        }
        args.replace(args.length()-1, args.length()-1, "");
        out.write("*(xr+ptx)= malloc(sizeof(int));".getBytes());
        out.write(String.format("*((int *)xr[ptx]) = syscall(%s);ptx++;", args).getBytes());
    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(this.astStatement.getArguments(), out);
    }
}
