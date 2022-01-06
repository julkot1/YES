package pl.julkot1.yes.statement.logic;

import pl.julkot1.yes.ast.models.AstStatement;

public class EGtStatement extends LogicStatement {
    public EGtStatement(AstStatement astStatement) {
        super(astStatement,"=>");
    }
}
