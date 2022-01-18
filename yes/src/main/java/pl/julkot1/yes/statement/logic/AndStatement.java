package pl.julkot1.yes.statement.logic;

import pl.julkot1.yes.ast.models.AstStatement;

public class AndStatement extends LogicStatement {
    public AndStatement(AstStatement astStatement) {
        super(astStatement, "&&");
    }
}
