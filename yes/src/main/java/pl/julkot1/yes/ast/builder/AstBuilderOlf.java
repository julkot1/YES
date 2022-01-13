package pl.julkot1.yes.ast.builder;

import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.ast.models.*;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.*;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AstBuilderOlf {
    public AstBuilderOlf(List<Token> tokens) {
        this.tokensList = tokens;
        ast = new AST();
    }
    private final List<Token> tokensList ;
    private final AST ast;


    public AST getAst() throws InvalidYesSyntaxException{
        getScope(tokensList, ast);
        return ast;
    }
    private void getScope(List<Token> list,IParental<?> parent) throws InvalidYesSyntaxException {
        var prefixes = new HashSet<PrefixTokens>();
        Type type = null;
        for (int i = 0; i < list.size(); i++) {
            var token = list.get(i);
            if (token.type().equals(TokenType.PREFIX)) getPrefix(token, prefixes);
            if (token.type().equals(TokenType.TYPE)) type = getType(token, type);
            if (token.type().equals(TokenType.STATEMENT)) {
                i += buildStatement(token, parent, i, prefixes, type, tokensList);
                prefixes = new HashSet<>();
            }

        }
    }
    private static void getPrefix(Token t, Set<PrefixTokens> prefixes) throws InvalidYesSyntaxException {
        var p = (PrefixTokens) t.obj();
        if (prefixes.contains(p)) throw new InvalidYesSyntaxException(t.line(), "prefix " + p.name()+" has been called more than 1 time");
        prefixes.add((PrefixTokens) t.obj());
    }
    private Type getType(Token t, Type type) throws InvalidYesSyntaxException {
        if(type!=null)throw new InvalidYesSyntaxException(t.line(), "multi type declaration is not allowed.");
        return Type.getTypeByYToken((String) t.obj()).get();
    }
    private int buildStatement(Token token, IParental<?> parent, int j, Set<PrefixTokens> prefixes, Type type, List<Token> tokens) throws InvalidYesSyntaxException{
        var currentStatement = new AstStatement((String) token.obj(), token.line(), getParent(parent));
        for (int i = j; i < tokens.size(); i++) {
            var t = tokens.get(i);
            if(t.obj().equals(SyntaxTokens.END_LINE)){
                if(type!=null)currentStatement.setType(type);
                addPrefixes(currentStatement, t, prefixes);
                parent.addToParent(currentStatement);
                return i-j;
            }else i+=buildValue(i, currentStatement, tokens);
        }
        return 0;
    }
    private void addPrefixes(Argument argument, Token t, Set<PrefixTokens> prefixes) {
        argument.getPrefixes().addAll(prefixes);
    }
    private Value parseValue(Token t, AstStatement currentStatement, Set<PrefixTokens> prefixes, Type type){
        var typeY = Type.NULL;
        if(type!=null)typeY = type;
        var value = new Value(typeY, (String) t.obj(), t.line(), currentStatement);
        addPrefixes(value, t, prefixes);
        return  value;
    }
    private int buildValue(int j, AstStatement currentStatement, List<Token> tokens) throws InvalidYesSyntaxException {
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
            }else {
                if(t.type().equals(TokenType.SPECIAL)) {
                    var b = (SpecialTypeTokens) t.obj();
                    if (b.isBool() || b.isPointer()) {
                        currentStatement.addArgument(parseValue(t, currentStatement, prefixes, type));
                        prefixes = new HashSet<>();
                    }
                }
                if (t.type().equals(TokenType.VALUE)) {
                    currentStatement.addArgument(parseValue(t, currentStatement, prefixes, type));
                    type = null;
                    prefixes = new HashSet<>();
                }
                if (t.type().equals(TokenType.ARRAY)) {
                    i += parseArray(t, currentStatement, prefixes, type, i, tokens);
                    type = null;
                    prefixes = new HashSet<>();
                }
                if(t.obj().equals(SyntaxTokens.NESTED_OPEN)) {
                    i+=buildNested(t, currentStatement, prefixes, type, i, tokens);
                    System.out.println(i);
                    type = null;
                    prefixes = new HashSet<>();
                }
            }
        }
        return 0;
    }

    private int buildNested(Token t, AstStatement currentStatement, HashSet<PrefixTokens> prefixes, Type type, int i, List<Token> tokens) throws InvalidYesSyntaxException {
        var typeY = Type.NULL;
        if(type!=null)typeY = type;
        var nStatement = new NestedStatement(typeY, t.line(), currentStatement);
        addPrefixes(nStatement, t,prefixes);
        var list = getNested(i+1, tokens);
        assert list != null;
        list.remove(list.size()-1);
        getScope(list, nStatement);
        currentStatement.addArgument(nStatement);
        return list.size();
    }
    private static List<Token> getNested( int i, List<Token> tokens) {
        var count = 1;
        var newList = new ArrayList<Token>();
        for (int j = i; j < tokens.size(); j++) {
            var token = tokens.get(j);
            if(token.obj().equals(SyntaxTokens.NESTED_OPEN))count++;
            else if(token.obj().equals(SyntaxTokens.NESTED_END))count--;
            newList.add(token);
            if(count==0)return newList;
        }
        return null;
    }
    private Token next(int i, List<Token> tokens){
        if(i+1 <= tokens.size())return tokens.get(i+1);
        return null;
    }
    private int parseArray(Token t, AstStatement currentStatement, HashSet<PrefixTokens> prefixes, Type type, int i, List<Token> tokens) throws InvalidYesSyntaxException {
        if(type==null) throw new InvalidYesSyntaxException(t.line(), "type of array must be specified");
        var n = next(i, tokens);
        assert n != null;
        var arr = new Array(type, t.obj().toString(),t.line(), currentStatement);
        var shift = IndexBuilder.parseIndex(arr, i, tokens);
        addPrefixes(arr, t, prefixes);
        currentStatement.addArgument(arr);

        return shift;
    }


    public static Argument getParent(IParental<?> parent) {
        if(parent.get() instanceof AST)return null;
        return (Argument) parent.get();
    }
}
