package pl.julkot1.yes.ast;

import lombok.Getter;
import pl.julkot1.yes.ast.builder.AstBuilder;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.IParental;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.Token;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AST implements IParental {
    private final List<AstStatement> statementList;
    public AST(){
        statementList = new ArrayList<>();
    }

    public static AST build(List<Token> tokens) throws InvalidYesSyntaxException {
        var builder = new AstBuilder(tokens);
        return builder.getAst();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        statementList.forEach(statement -> stringBuilder.append(statement).append("\n"));
        return stringBuilder.toString();
    }

    @Override
    public void addToParent(Argument argument) {
        statementList.add((AstStatement) argument);
    }

    @Override
    public Object get() {
        return this;
    }
}
