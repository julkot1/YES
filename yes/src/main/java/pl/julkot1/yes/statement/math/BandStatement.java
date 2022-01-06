package pl.julkot1.yes.statement.math;

import pl.julkot1.yes.ast.models.AstStatement;

public class BandStatement extends MathStatement{
    public BandStatement(AstStatement astStatement) {
        super(astStatement, "&");
    }
}