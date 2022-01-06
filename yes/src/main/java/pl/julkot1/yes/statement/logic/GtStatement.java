package pl.julkot1.yes.statement.logic;

import pl.julkot1.yes.ast.models.AstStatement;

public class GtStatement extends LogicStatement {
    public GtStatement(AstStatement astStatement) {
        super(astStatement, ">");
    }
}
