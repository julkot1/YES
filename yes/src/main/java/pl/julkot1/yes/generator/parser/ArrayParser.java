package pl.julkot1.yes.generator.parser;

import org.apache.commons.lang3.math.NumberUtils;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.exception.InvalidArrayIndexException;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
import pl.julkot1.yes.lexer.tokens.SpecialTypeTokens;
import pl.julkot1.yes.types.Type;

public class ArrayParser {
    public static String parseIndex(Array array) throws InvalidYesSyntaxException {
         if(array.getToken().equals(SpecialTypeTokens.AR.getToken())){
            if(!NumberUtils.isDigits(array.getIndex().getArgument().getToken()))throw new InvalidYesSyntaxException(array.getLine(), "index must be a constance value");
            return array.getIndex().getArgument().getToken();
        }
        else{
            String pointer = "pt" + array.getToken().charAt(0);
            String sub = "1";
            if (array.getIndex().getArgument() instanceof Value) {
                if (SpecialTypeTokens.isPointer(array.getIndex().getArgument().getToken())) sub = array.getIndex().getArgument().getToken();
                else sub = "1-" + array.getIndex().getArgument().getToken();

            }
            if (array.getIndex().getArgument() instanceof AstStatement)
                sub = "1-*((unsigned long*) rx)";
            if (array.getIndex().getArgument() instanceof Array)
                sub = getElement((Array) array.getIndex().getArgument());
            return "[" + pointer + "-" + sub + "]";
        }
    }

    public static String getElement(Array array) throws InvalidYesSyntaxException {
        String index = parseIndex(array);
        String arr = array.getToken();
        String type = array.getType().getCToken();
        String returning;
        if (PrefixTokens.REFERENCE.hasPrefix(array.getPrefixes()))
            returning =  arr + index;
        else if(array.getToken().equals(SpecialTypeTokens.AR.getToken()))
            returning = "ar"+index;
        else if(array.getType().equals(Type.STR))
            returning = "((" + type + "*)" + arr + index + ")";
        else returning = "*((" + type + "*)" + arr + index + ")";
        if( array.getType().equals(Type.CHAR))
            return "("+returning+"-'0')";
        return returning;
    }
}
