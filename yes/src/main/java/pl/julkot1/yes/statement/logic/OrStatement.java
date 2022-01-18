package pl.julkot1.yes.statement.logic;

import pl.julkot1.yes.ast.models.AstStatement;

public class OrStatement extends LogicStatement {
    public OrStatement(AstStatement astStatement) {
        super(astStatement, "||");
    }
}
