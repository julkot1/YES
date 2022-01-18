package pl.julkot1.yes.statement.math;

import pl.julkot1.yes.ast.models.AstStatement;

public class MulStatement extends MathStatement{
    public MulStatement(AstStatement astStatement) {
        super(astStatement, "*");
    }
}