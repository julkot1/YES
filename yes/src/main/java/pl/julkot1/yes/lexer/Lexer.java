package pl.julkot1.yes.lexer;

import org.javatuples.Pair;
import pl.julkot1.yes.lexer.tokens.*;
import pl.julkot1.yes.statement.StatementTokens;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Lexer {
    private static List<String> getLines(BufferedReader in){
        return in.lines()
                .map(s -> s.strip().replace("\n", "").replace("\r", ""))
                .collect(Collectors.toList());
    }
    public static void resolveFile(String fileName) throws IOException{
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        var lines = getLines(in);
        in.close();
        long index = 0;
        for(String line : lines){
            index++;
            getTokens(line, index).forEach(System.out::println);
        }

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
    public static List<Token> getTokens(String chars, long line){
        var tokens = new ArrayList<Token>();
        StringBuilder buffer = new StringBuilder();
        StringBuilder strBuffer = new StringBuilder();
        var isString = false;
        var newLine = true;
        var bytes = chars.getBytes();
        for(int i = 0; i < bytes.length; i++ ){
            char c = (char)bytes[i];

            var t = getSyntaxToken( c, line);
            if(!isString){
                if(t != null){
                    if(t.obj().equals(SyntaxTokens.STRING)) isString = true;
                    if(t.obj().equals(SyntaxTokens.END_LINE) || t.obj().equals(SyntaxTokens.NESTED_OPEN)) newLine = true;
                    if(t.obj().equals(SyntaxTokens.COMMENT))return  tokens ;
                    tokens.add(t);
                }else {
                    var p = getPrefixToken(c, line);
                    if (p == null) {
                        buffer.append(c);
                        var token = getTokensFromBuffer(buffer.toString(), line, newLine, i+1<bytes.length? (char) bytes[i+1]: 'i');
                        if(token != null) {
                            if (token.type() == TokenType.STATEMENT && newLine) newLine = false;
                            tokens.add(token);
                            buffer = new StringBuilder();
                        }
                    }else{
                        tokens.add(p);
                    }
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
        return tokens;

    }

    private static Token getTokensFromBuffer(String buffer, long line, boolean newLine, char next) {
        var s = SpecialTypeTokens.getToken(buffer);
        var nextS = SyntaxTokens.getToken(next);
        if(s.isEmpty() && (buffer.endsWith(" ") || nextS.isPresent()) && !buffer.equals(" ")){
            if(newLine){
                return new Token(buffer.replace(" ", ""), line,TokenType.STATEMENT);
            } return new Token(buffer.replace(" ", ""), line,TokenType.VALUE);
        }else if(s.isPresent()) return new Token(s.get(), line, TokenType.SPECIAL);
        return null;
    }
}
