package pl.julkot1.yes.util;

import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.statement.StatementTokens;

public class ArgumentsArray {
    public static boolean isInStatementDeclaration(Argument argument){
        if(argument.getParent() == null) return false;
        if(!(argument.getParent() instanceof NestedStatement))
            if(argument.getParent().getToken().equals(StatementTokens.STATEMENT_DEF.getToken()))return true;
        return isInStatementDeclaration(argument.getParent());
    }
}
