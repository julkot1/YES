package pl.julkot1.yes.ast.builder;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.ast.scope.IterationCondition;
import pl.julkot1.yes.ast.scope.Scope;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
import pl.julkot1.yes.lexer.tokens.SyntaxTokens;
import pl.julkot1.yes.lexer.tokens.TokenType;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class NestedBuilder extends Builder<NestedStatement> {
    @Override
    protected void build() throws InvalidYesSyntaxException {
        var type = Type.NULL;
        if(this.type != Type.NULL)type = this.type;
        inst = new NestedStatement(type, parent.getLine(), parent);
        while (scope.getTokens().size()!=0){
            inst.addToParent(getStatement());
        }
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
                case STATEMENT -> statement.set((StatementBuilder) new StatementBuilder().parse(type, prefixes, scope, index, null));
                case TYPE -> {
                    if(type!=null)throw new TypeException(t.line(), t.toString(), "unfortunately multi type declaration is not allowed.");
                    type = Type.getTypeByYToken((String) t.obj()).get();
                }
                case PREFIX -> prefixes.add((PrefixTokens) t.obj());
                default -> throw new InvalidYesSyntaxException(t.line(), "Nobody expects "+t+" token!");
            }
            return true;
        }, condition);
        return statement.get().inst;
    }
    @Override
    protected void createScope(Scope rawScope, int shift) throws InvalidYesSyntaxException {
        scope = new Scope(0, parent);
        AtomicInteger count = new AtomicInteger(0);
        rawScope.updateTokens();
        rawScope.iterate((t, prev, next, index) -> {
            if(t.obj().equals(SyntaxTokens.NESTED_OPEN))count.accumulateAndGet(1, Integer::sum);
            else if(t.obj().equals(SyntaxTokens.NESTED_END))count.accumulateAndGet(1, (left, right) -> left-right);
            scope.getTokens().add(t);
            return count.get() != 0;
        });
        scope.getTokens().remove(0);
        if(scope.getTokens().size()!=0)scope.getTokens().remove(scope.getTokens().size()-1);
        rawScope.shift(scope.getTokens().size()+1);
        rawScope.updateTokens();
    }
}
