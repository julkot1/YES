package pl.julkot1.yes.statement.math;

import pl.julkot1.yes.ast.models.AstStatement;

public class AddStatement extends MathStatement{
    public AddStatement(AstStatement astStatement) {
        super(astStatement, "+");
    }
}
