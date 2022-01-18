package pl.julkot1.yes.ast.scope;

import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.Token;

import java.util.List;

@FunctionalInterface
public interface IterationCondition {
    boolean execute(Token t, Token prev, Token next, int i) throws InvalidYesSyntaxException;
    default void iterate(List<Token> tokens, ScopeFunctions sf) throws InvalidYesSyntaxException {
        for(int i =0; i < tokens.size(); i++){
            Token t = tokens.get(i);
            Token prev = null;
            Token next = null;
            if(i!=0)prev = tokens.get(i-1);
            if(i+1<tokens.size())next = tokens.get(i+1);
            if(execute(t, prev, next, i))sf.iteration(t,prev, next, i);
            else break;
        }
    }
}
