package pl.julkot1.yes.statement.logic;

import pl.julkot1.yes.ast.models.AstStatement;

public class EqStatement extends LogicStatement {
    public EqStatement(AstStatement astStatement) {
        super(astStatement, "==");
    }
}
