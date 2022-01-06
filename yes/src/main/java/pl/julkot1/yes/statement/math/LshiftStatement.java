package pl.julkot1.yes.statement.math;

import pl.julkot1.yes.ast.models.AstStatement;

public class LshiftStatement extends MathStatement{
    public LshiftStatement(AstStatement astStatement) {
        super(astStatement, "<<");
    }
}