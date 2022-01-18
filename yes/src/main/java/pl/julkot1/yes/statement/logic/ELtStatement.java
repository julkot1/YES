package pl.julkot1.yes.statement.logic;

import pl.julkot1.yes.ast.models.AstStatement;

public class ELtStatement extends LogicStatement {
    public ELtStatement(AstStatement astStatement) {
        super(astStatement, "=<");
    }
}
