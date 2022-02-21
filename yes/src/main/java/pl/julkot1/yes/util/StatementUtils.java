package pl.julkot1.yes.util;

import pl.julkot1.yes.ast.models.Argument;


public class StatementUtils  {
    public static boolean isStatementIn(Argument statement, String in){
        if(statement.getParent()==null)
            return false;
        if(statement.getParent().getToken()!=null)
            if(statement.getParent().getToken().equals(in))
                return true;
        return isStatementIn(statement.getParent(), in);
    }
}
