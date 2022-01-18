package pl.julkot1.yes.statement.math;

import pl.julkot1.yes.ast.models.AstStatement;

public class BorStatement extends MathStatement{
    public BorStatement(AstStatement astStatement) {
        super(astStatement, "|");
    }
}