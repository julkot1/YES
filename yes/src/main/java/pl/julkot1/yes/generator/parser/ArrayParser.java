package pl.julkot1.yes.generator.parser;

import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.exception.InvalidArrayIndexException;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
import pl.julkot1.yes.lexer.tokens.SpecialTypeTokens;
import pl.julkot1.yes.prefix.CallParentCr;

public class ArrayParser {
    public static String parseIndex(Array array) throws InvalidYesSyntaxException {
        String pointer = "pt" + array.getToken().charAt(0);
        if (array.getToken().equals(SpecialTypeTokens.PR.getToken())) {
            if (!CallParentCr.hasCalled(array))
                throw new InvalidArrayIndexException(array.getLine(), array.getToken(),
                        "to use pr, prefix $ must be used in parent statement");
            pointer = "*ptp";
        }
        String sub = "1";
        if (array.getIndex() instanceof Value)
            sub = "1-" + array.getIndex().getToken();
        if (array.getIndex() instanceof AstStatement)
            sub = "1-*((unsigned long*) xr[ptx-1])";
        if (array.getIndex() instanceof Array)
            sub = getElement((Array) array.getIndex());
        return "[" + pointer + "-" + sub + "]";
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
