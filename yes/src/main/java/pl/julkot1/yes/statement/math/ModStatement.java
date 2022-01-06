package pl.julkot1.yes.statement.math;

import pl.julkot1.yes.ast.models.AstStatement;

public class ModStatement extends MathStatement{
    public ModStatement(AstStatement astStatement) {
        super(astStatement, "%");
    }
}