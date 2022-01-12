package pl.julkot1.yes.ast.scope;

import lombok.Getter;
import lombok.Setter;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.Token;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Scope {
    private List<Token> tokens;
    @Setter
    private Argument parent;
    private final int scopeBegin;
    private int shiftCount = 0;
    public Scope(int scopeBegin, Argument parent) {
        this.scopeBegin = scopeBegin;
        this.tokens = new ArrayList<>();
        this.parent = parent;
    }

    /**
     * Increase shift count value by {toShift}
     * @param toShift shift value
     */
    public void shift(int toShift){
        shiftCount+=toShift;
    }
    public void setTokens(List<Token> tokens, IterationCondition condition) throws InvalidYesSyntaxException {
        this.tokens = new ArrayList<>();
        ScopeFunctions sf = (t, prev, next, i) ->this.tokens.add(t);
        condition.iterate(tokens.subList(scopeBegin, tokens.size()), sf);
    }
    public void setTokens(List<Token> tokens){
        this.tokens = tokens;
    }
    public void iterate(ScopeFunctions sf) throws InvalidYesSyntaxException {
        sf.iterate(tokens);
    }
    public void iterateConditional(ScopeFunctions sf,  IterationCondition condition) throws InvalidYesSyntaxException {
        condition.iterate(tokens, sf);
    }
    public void updateTokens(){
        tokens = tokens.subList(shiftCount, tokens.size());
        shiftCount = 0;
    }


}
