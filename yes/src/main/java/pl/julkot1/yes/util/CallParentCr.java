package pl.julkot1.yes.util;

import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;

public class CallParentCr {
    public static boolean hasCalled(Argument argument){
        if( PrefixTokens.CALL_PARENT_CR.hasPrefix(argument.getPrefixes()))
                return true;
        if(argument.getParent()==null)
            return false;
        return hasCalled( argument.getParent());
    }
}
