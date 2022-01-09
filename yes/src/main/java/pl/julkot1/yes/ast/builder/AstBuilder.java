package pl.julkot1.yes.ast.builder;

import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.IParental;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
import pl.julkot1.yes.lexer.tokens.SyntaxTokens;
import pl.julkot1.yes.lexer.tokens.Token;
import pl.julkot1.yes.lexer.tokens.TokenType;
import pl.julkot1.yes.types.Type;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AstBuilder {
    public AstBuilder(List<Token> tokens) {
        this.tokens = tokens;
        ast = new AST();
    }

    private final AST ast;
    private final List<Token> tokens;

    public AST getAst() throws InvalidYesSyntaxException{
        var prefixes = new HashSet<PrefixTokens>();
        Type type = null;
        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);
            if(token.type().equals(TokenType.PREFIX))getPrefix(token, prefixes);
            if(token.type().equals(TokenType.TYPE))type = getType(token, type);
            if(token.type().equals(TokenType.STATEMENT)) {
                i += buildStatement(token, ast, i, prefixes, type);
                prefixes = new HashSet<>();
            }
        }
        return ast;
    }
    private void getPrefix(Token t, Set<PrefixTokens> prefixes) throws InvalidYesSyntaxException {
        var p = (PrefixTokens) t.obj();
        if (prefixes.contains(p)) throw new InvalidYesSyntaxException(t.line(), "prefix " + p.name()+" has been called more than 1 time");
        prefixes.add((PrefixTokens) t.obj());
    }
    private Type getType(Token t, Type type) throws InvalidYesSyntaxException {
        if(type!=null)throw new InvalidYesSyntaxException(t.line(), "multi type declaration is not allowed.");
        return Type.getTypeByYToken((String) t.obj()).get();
    }
    private int buildStatement(Token token, IParental<?>  parent, int j, Set<PrefixTokens> prefixes,  Type type) throws InvalidYesSyntaxException{
        var currentStatement = new AstStatement((String) token.obj(), token.line(), getParent(parent));
        for (int i = j; i < tokens.size(); i++) {
            var t = tokens.get(i);
            if(t.obj().equals(SyntaxTokens.END_LINE)){
                if(type!=null)currentStatement.setType(type);
                addPrefixes(currentStatement, t, prefixes);
                parent.addToParent(currentStatement);
                return i-j;
            }else i+=buildValue(i, currentStatement);
        }
        return 0;
    }
    private void addPrefixes(Argument argument, Token t, Set<PrefixTokens> prefixes) {
        for (PrefixTokens prefix : prefixes)
            prefix.valid(argument.getClass(), t);
        argument.getPrefixes().addAll(prefixes);
    }
    private Value parseValue(Token t, AstStatement currentStatement, Set<PrefixTokens> prefixes, Type type){
        var typeY = Type.NULL;
        if(type!=null)typeY = type;
        var value = new Value(typeY, (String) t.obj(), t.line(), currentStatement);
        addPrefixes(value, t, prefixes);
        return  value;
    }
    private int buildValue( int j, AstStatement currentStatement) throws InvalidYesSyntaxException {
        var prefixes = new HashSet<PrefixTokens>();
        Type type = null;
        for (int i = j+1; i < tokens.size(); i++) {
            var t = tokens.get(i);
            if(t.type().equals(TokenType.PREFIX)) {
                getPrefix(t, prefixes);
            }
            if(t.type().equals(TokenType.TYPE))type = getType(t,type);
            if(t.obj().equals(SyntaxTokens.END_LINE)){
                return i-j-1;
            }else if(t.type().equals(TokenType.VALUE))
                currentStatement.addArgument(parseValue(t, currentStatement, prefixes, type));

        }
        return 0;
    }

    public static Argument getParent(IParental<?> parent) {
        if(parent.get() instanceof AST)return null;
        return (Argument) parent.get();
    }
}
