package pl.julkot1.yes.statement.math;

import pl.julkot1.yes.ast.models.AstStatement;

public class SubStatement extends MathStatement{
    public SubStatement(AstStatement astStatement) {
        super(astStatement, "-");
    }
}