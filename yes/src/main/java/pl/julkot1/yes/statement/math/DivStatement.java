package pl.julkot1.yes.statement.math;

import pl.julkot1.yes.ast.models.AstStatement;

public class DivStatement extends MathStatement{
    public DivStatement(AstStatement astStatement) {
        super(astStatement, "/");
    }
}