package pl.julkot1.yes.generator.parser;

import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.lexer.PrefixTokens;
import pl.julkot1.yes.lexer.SpecialTypeTokens;

public class ArrayParser {
    public static String parseIndex(Array array){
        String pointer = "pt"+array.getToken().charAt(0);
        if (array.getToken().equals(SpecialTypeTokens.PR.getToken()))pointer="*ptp";
        String sub = "1";
        if (array.getIndex() instanceof Value) sub = "1-"+array.getIndex().getToken();
        if (array.getIndex() instanceof AstStatement)sub = "1-*((unsigned long*) xr[ptx-1])";
        if (array.getIndex() instanceof Array)sub = getElement((Array) array.getIndex());
        return  "["+pointer+"-"+sub+"]";
    }
    public static String getElement(Array array){
        String index = parseIndex(array);
        String arr = array.getToken();
        String type = array.getType().getCToken();
        if(PrefixTokens.REFERENCE.hasPrefix(array.getPrefixes()))
            return arr+index;
        return "*(("+type+"*)"+arr+index+")";
    }
}
