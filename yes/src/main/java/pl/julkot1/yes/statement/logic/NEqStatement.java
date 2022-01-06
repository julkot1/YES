package pl.julkot1.yes.statement.logic;

import pl.julkot1.yes.ast.models.AstStatement;

public class NEqStatement extends LogicStatement {
    public NEqStatement(AstStatement astStatement) {
        super(astStatement, "!=");
    }
}
