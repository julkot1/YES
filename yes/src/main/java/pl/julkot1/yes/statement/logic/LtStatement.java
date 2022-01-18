package pl.julkot1.yes.statement.logic;

import pl.julkot1.yes.ast.models.AstStatement;

public class LtStatement extends LogicStatement {
    public LtStatement(AstStatement astStatement) {
        super(astStatement, "<");
    }
}
