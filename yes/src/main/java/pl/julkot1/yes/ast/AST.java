package pl.julkot1.yes.ast;

import lombok.Getter;
import pl.julkot1.yes.ast.models.AstStatement;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AST {
    private final List<AstStatement> statementList;
    public AST(){
        statementList = new ArrayList<>();
    }
}
