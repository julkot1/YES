package pl.julkot1.yes.statement.math;

import pl.julkot1.yes.ast.models.AstStatement;

public class XorStatement extends MathStatement{
    public XorStatement(AstStatement astStatement) {
        super(astStatement, "^");
    }
}