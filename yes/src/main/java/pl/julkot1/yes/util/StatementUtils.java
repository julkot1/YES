package pl.julkot1.yes.util;

import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.statement.custom.interfaces.ArgumentCount;
import pl.julkot1.yes.types.Type;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;


public class StatementUtils  {
    public static boolean isStatementIn(Argument statement, String in){
        if(statement.getParent()==null)
            return false;
        if(statement.getParent().getToken()!=null)
            if(statement.getParent().getToken().equals(in))
                return true;
        return isStatementIn(statement.getParent(), in);
    }
    public static int argumentsSize(List<ArgumentCount> argumentCounts){
        AtomicInteger size = new AtomicInteger();
        argumentCounts.forEach(arg ->
            size.addAndGet(arg.getValue())
        );
        return size.get();
    }
    public static Type argumentType(List<ArgumentCount> argumentCounts, int index){
        var pos = 0;
        for (ArgumentCount arg : argumentCounts) {
            pos += arg.getValue();
            if(index<=pos){
                return arg.getType();
            }
        }
        return Type.NULL;
    }
    public static Optional<AstStatement> getParent(Argument argument, String parent){
        if(argument.getParent()==null)
            return Optional.empty();
        if(argument.getParent().getToken()!=null)
            if(argument.getParent().getToken().equals(parent))
                return Optional.of((AstStatement) argument.getParent());
        return getParent(argument.getParent(), parent);
    }
}
