package pl.julkot1.yes.lexer;

import org.javatuples.Pair;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.*;
import pl.julkot1.yes.statement.StatementTokens;
import pl.julkot1.yes.types.Type;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class Lexer {
    private static List<String> getLines(BufferedReader in){
        return in.lines()
                .map(s -> s.strip().replace("\n", "").replace("\r", ""))
                .collect(Collectors.toList());
    }
    public static List<Token> resolve(String code) throws  InvalidYesSyntaxException{
        return getTokens(code, 1);
    }
    public static List<Token> resolveFile(String fileName) throws IOException, InvalidYesSyntaxException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        var lines = getLines(in);
        var tokens = new ArrayList<Token>();
        in.close();
        long index = 0;
        for(String line : lines){
            index++;
            tokens.addAll(getTokens(line, index));
        }
        return tokens;
    }
    private static Token getSyntaxToken(char c, long line){
        var t = SyntaxTokens.getToken(c);
        if(t.isEmpty())return null;
        return new Token(t.get(), line, TokenType.SYNTAX);
    }
    private static Token getPrefixToken(char c, long line){
        var t = PrefixTokens.getToken(c);
        if(t.isEmpty())return null;
        return new Token(t.get(), line, TokenType.PREFIX);
    }
    public static List<Token> getTokens(String chars, long line) throws InvalidYesSyntaxException {
        var tokens = new ArrayList<Token>();
        StringBuilder buffer = new StringBuilder();
        StringBuilder strBuffer = new StringBuilder();
        var isString = false;
        var newLine = true;
        var type = false;
        var bytes = chars.getBytes();
        for(int i = 0; i < bytes.length; i++ ){
            char c = (char)bytes[i];

            var t = getSyntaxToken( c, line);
            if(!isString){
                if(t != null){
                    if(t.obj().equals(SyntaxTokens.CAST_OPEN)) type = true;
                    if(t.obj().equals(SyntaxTokens.CAST_END)) type = false;
                    if(t.obj().equals(SyntaxTokens.STRING)) isString = true;
                    if(t.obj().equals(SyntaxTokens.END_LINE) || t.obj().equals(SyntaxTokens.NESTED_OPEN)|| t.obj().equals(SyntaxTokens.REGISTER_OPEN)) newLine = true;
                    if(t.obj().equals(SyntaxTokens.REGISTER_END) || t.obj().equals(SyntaxTokens.NESTED_END))newLine=false;
                    if(t.obj().equals(SyntaxTokens.COMMENT))return  tokens;
                    tokens.add(t);
                }else {
                    var p = getPrefixToken(c, line);
                    if (p == null) {
                        buffer.append(c);
                        var token = getTokensFromBuffer(buffer.toString(), line, newLine, i+1<bytes.length? (char) bytes[i+1]: 'i', type);
                        if(token != null) {
                            if (token.type() == TokenType.STATEMENT && newLine) newLine = false;
                            tokens.add(token);
                            buffer = new StringBuilder();
                        }
                    }else tokens.add(p);
                }

            }else {
                if(t != null){
                    if(t.obj().equals(SyntaxTokens.STRING)) {
                        isString = false;
                        tokens.add(new Token(strBuffer.toString(), line, TokenType.VALUE));
                        strBuffer = new StringBuilder();
                        tokens.add(t);
                    }
                }else{
                    strBuffer.append(c);
                }
            }
        }
        validTokens(tokens);
        return tokens;

    }
    private static void validTokens(List<Token> tokens) throws InvalidYesSyntaxException{
        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);
            if(token.type().equals(TokenType.TYPE)){
                var prev = next(tokens, i-2);
                var next = next(tokens, i);
                if(prev == null) throw new InvalidYesSyntaxException(token.line(),  "Invalid type cast declaration (missing open token)");
                if(next == null) throw new InvalidYesSyntaxException(token.line(),  "Invalid type cast declaration (missing end token)");
                if(!next.obj().equals(SyntaxTokens.CAST_END)) throw new InvalidYesSyntaxException(token.line(),  "Invalid type cast declaration (missing end token)");
                if(!prev.obj().equals(SyntaxTokens.CAST_OPEN)) throw new InvalidYesSyntaxException(token.line(),  "Invalid type cast declaration (missing open token)");
            }
        }
    }
    private static Token next(List<Token> tokens, int i){
        if(i+1 <= tokens.size())return tokens.get(i+1);
        return null;
    }

    private static Token simplifyString(List<Token> tokens, int i, Token token) throws InvalidYesSyntaxException{
        var val = next(tokens, i);
        var end = next(tokens, i+1);
        if (end == null)throw new InvalidYesSyntaxException(token.line(), "Invalid Str value declaration");
        if(!end.obj().equals(SyntaxTokens.STRING))throw new InvalidYesSyntaxException(token.line(), "Invalid Str value declaration");
        if(val==null)return new Token("\"\"", token.line(), TokenType.VALUE);
        else if(val.type().equals(TokenType.VALUE)) return new Token("\""+val.obj()+"\"", val.line(), TokenType.VALUE);
        else throw new InvalidYesSyntaxException(token.line(), "Invalid Str value declaration");
    }
    private static Token simplifyType(List<Token> tokens, int i, Token token) throws InvalidYesSyntaxException{
        var val = next(tokens, i);
        var end = next(tokens, i+1);
        if (end == null)throw new InvalidYesSyntaxException(token.line(), "Invalid type cast declaration (missing end token)");
        if(!end.obj().equals(SyntaxTokens.CAST_END))throw new InvalidYesSyntaxException(token.line(), "Invalid type cast declaration (missing end token)");
        if(val==null)throw new InvalidYesSyntaxException(token.line(), "Missing type declaration");
        if(!val.type().equals(TokenType.TYPE))throw new InvalidYesSyntaxException(token.line(), "Between cast brackets there must be a type's token");
        return new Token(val.obj(), val.line(), TokenType.TYPE);
    }
    public static List<Token> simplify(List<Token> tokens) throws InvalidYesSyntaxException {
        var newTokens = new ArrayList<Token>();
        for (int i = 0; i < tokens.size(); i++) {
            var token = tokens.get(i);
            if(token.obj().equals(SyntaxTokens.STRING)){
               newTokens.add(simplifyString(tokens, i, token));
               i+=2;
            }else if(token.obj().equals(SyntaxTokens.CAST_OPEN)){
                newTokens.add(simplifyType(tokens, i, token));
                i+=2;
            }else newTokens.add(token);
            if(token.obj().equals(SyntaxTokens.REGISTER_OPEN)) {
                var val = next(tokens, i);
                var end = next(tokens, i+1);
                if (end!=null && val != null){
                    if(end.obj().equals(SyntaxTokens.REGISTER_END)){
                        if(SpecialTypeTokens.isArray(val.toString())) newTokens.add(new Token((val.toString().toLowerCase(Locale.ROOT)), val.line(), TokenType.SPECIAL));
                        else newTokens.add(new Token(val.toString(), val.line(), TokenType.VALUE));
                        i++;
                    }
                }
            }
        }
        return newTokens;
    }



    private static Token getTokensFromBuffer(String buffer, long line, boolean newLine, char next, boolean type) {
        var s = SpecialTypeTokens.getToken(buffer);
        var nextS = SyntaxTokens.getToken(next);
        if(s.isEmpty() && (buffer.endsWith(" ") || nextS.isPresent()) && !buffer.equals(" ")){
            var token = buffer.replace(" ", "");
            if(newLine && !type)
                return new Token(token, line,TokenType.STATEMENT);
            if(Type.getTypeByYToken(token).isPresent())
                return new Token(token, line,TokenType.TYPE);
            else if(SpecialTypeTokens.isArray(token))return new Token(token, line,TokenType.ARRAY);
            else return new Token(token, line,TokenType.VALUE);
        }else if(s.isPresent()) return new Token(s.get().getToken(), line, TokenType.VALUE);
        return null;
    }
}
