package pl.julkot1.yes.ast.builder;

import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.scope.IterationCondition;
import pl.julkot1.yes.ast.scope.Scope;
import pl.julkot1.yes.ast.scope.StatementBuilder;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
import pl.julkot1.yes.lexer.tokens.Token;
import pl.julkot1.yes.lexer.tokens.TokenType;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AstBuilder {
    private Scope scope;
    private List<PrefixTokens> prefixes;
    private Type type;
    public AstBuilder(List<Token> tokens) {
        this.scope = new Scope(0, null);
        this.scope.setTokens(tokens);
        ast = new AST();
    }
    private final AST ast;
    public AST getAst() throws InvalidYesSyntaxException {
        while (scope.getTokens().size()!=0){
            ast.addToParent(getStatement());
        }
        return ast;
    }
    private AstStatement getStatement() throws InvalidYesSyntaxException {
        type = null;
        prefixes = new ArrayList<>();
        AtomicReference<StatementBuilder> statement = new AtomicReference<>();
        IterationCondition condition = (t, prev, next, index) -> {
            if(prev == null)return true;
            return !prev.type().equals(TokenType.STATEMENT);
        };
        scope.iterateConditional((t, prev, next, index) -> {
            switch (t.type()){
                case STATEMENT -> statement.set((StatementBuilder) new StatementBuilder().parse(type, prefixes, scope, index));
                case PREFIX, SPECIAL, VALUE, SYNTAX ->{}
                default -> throw new InvalidYesSyntaxException(t.line(), "Nobody expects "+t+" token!");
            }
            return true;
        }, condition);
        return statement.get().inst;
    }
}
