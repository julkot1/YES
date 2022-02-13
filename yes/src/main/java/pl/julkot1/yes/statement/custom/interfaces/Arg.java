package pl.julkot1.yes.statement.custom.interfaces;

import org.apache.commons.lang3.math.NumberUtils;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;

import java.util.List;

public class Arg extends AbstractArg{
    public Arg(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void initValidArg() throws InvalidYesSyntaxException {
        if(astStatement.getArguments().size()>1)   throw new InvalidArgumentsQuantity(astStatement.getLine(),  astStatement.getToken());
    }

    @Override
    public int applyArg(List<ArgumentCount> argumentCounts) {
        if(astStatement.getArguments().size()==0)argumentCounts.add(new ArgumentCount(1, astStatement.getType()));
        if(astStatement.getArguments().size()==1) {
            var num = NumberUtils.toInt(astStatement.getArgument(0).getToken());
            argumentCounts.add(new ArgumentCount(num, astStatement.getType()));
            return num;
        }
        return 1;
    }
}
