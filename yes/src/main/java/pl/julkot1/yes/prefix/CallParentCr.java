package pl.julkot1.yes.prefix;

import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.lexer.PrefixTokens;

public class CallParentCr {
    public static boolean hasCalled(Argument argument){
        if( PrefixTokens.CALL_PARENT_CR.hasPrefix(argument.getPrefixes()))
                return true;
        if(argument.getParent()==null)
            return false;
        return hasCalled( argument.getParent());
    }
}
