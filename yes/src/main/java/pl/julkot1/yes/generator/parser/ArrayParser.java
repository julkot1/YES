package pl.julkot1.yes.generator.parser;

import org.apache.commons.lang3.math.NumberUtils;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.exception.InvalidArrayIndexException;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
import pl.julkot1.yes.lexer.tokens.SpecialTypeTokens;

public class ArrayParser {
    public static String parseIndex(Array array) throws InvalidYesSyntaxException {
        if(array.getToken().equals(SpecialTypeTokens.XR.getToken())){
            if(!NumberUtils.isDigits(array.getIndex().getToken()))throw new InvalidYesSyntaxException(array.getLine(), "index must be a constance value");
            return "["+array.getIndex().getToken()+"]";
        }
        else{
            String pointer = "pt" + array.getToken().charAt(0);
            String sub = "1";
            if (array.getIndex() instanceof Value) {
                if (SpecialTypeTokens.isPointer(array.getIndex().getToken())) sub = array.getIndex().getToken();
                else sub = "1-" + array.getIndex().getToken();

            }
            if (array.getIndex() instanceof AstStatement)
                sub = "1-*((unsigned long*) xr[0])";
            if (array.getIndex() instanceof Array)
                sub = getElement((Array) array.getIndex());
            return "[" + pointer + "-" + sub + "]";
        }
    }

    public static String getElement(Array array) throws InvalidYesSyntaxException {
        String index = parseIndex(array);
        String arr = array.getToken();
        String type = array.getType().getCToken();
        if (PrefixTokens.REFERENCE.hasPrefix(array.getPrefixes()))
            return arr + index;
        return "*((" + type + "*)" + arr + index + ")";
    }
}
