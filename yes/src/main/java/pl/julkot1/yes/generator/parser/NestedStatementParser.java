package pl.julkot1.yes.generator.parser;

import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;

public class NestedStatementParser {
    public static boolean isSingleStatement(NestedStatement statement) {
        return statement.getStack().size() == 1;
    }

}
