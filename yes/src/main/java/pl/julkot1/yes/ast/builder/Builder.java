package pl.julkot1.yes.ast.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.scope.Scope;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
import pl.julkot1.yes.lexer.tokens.Token;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
public abstract class Builder <T extends Argument>{
    protected Scope scope;
    protected Argument parent;
    protected List<PrefixTokens> prefixes;
    protected String namespace;
    protected Type type;
    @Getter
    protected  T inst;
    @Getter
    protected int shift = 0, absShift = 0;
    protected abstract void build() throws InvalidYesSyntaxException;
    protected abstract void createScope(Scope rawScope, int shift) throws InvalidYesSyntaxException;
    public Builder<T> parse(Type type, List<PrefixTokens> prefixes, Scope rawScope, int shift, Argument parent, String namespace) throws InvalidYesSyntaxException {
        this.prefixes = prefixes;
        this.type =type;
        this.namespace = namespace;
        this.parent = parent;
        createScope(rawScope, shift);
        build();
        return this;
    }
    protected void setType(Token token)throws InvalidYesSyntaxException{
        if(this.type!=null)throw new InvalidYesSyntaxException(token.line(), "unfortunately multi type declaration is not allowed.");
        this.type = Type.getTypeByYToken(token.toString()).get();
    }
    protected void getPrefix(Token t) throws InvalidYesSyntaxException {
        var p = (PrefixTokens) t.obj();
        if (prefixes.contains(p)) throw new InvalidYesSyntaxException(t.line(), "oh no! prefix " + p.name()+" has been called more than 1 time");
        prefixes.add((PrefixTokens) t.obj());
    }
    protected void addPrefixes(Argument argument) {
        for (PrefixTokens prefix : prefixes)
            prefix.valid(argument);
        argument.getPrefixes().addAll(prefixes);
        prefixes = new ArrayList<>();
    }
}
