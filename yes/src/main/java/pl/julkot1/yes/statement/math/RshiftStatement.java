package pl.julkot1.yes.statement.math;

import pl.julkot1.yes.ast.models.AstStatement;

public class RshiftStatement extends MathStatement{
    public RshiftStatement(AstStatement astStatement) {
        super(astStatement, ">>");
    }
}