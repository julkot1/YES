package pl.julkot1;

import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.CGenerator;
import pl.julkot1.yes.lexer.Lexer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try{
            var pre = Lexer.resolveFile(args[0]);
            var t = Lexer.simplify(pre);
            var a = AST.build(t);
            CGenerator.generate(a, "out.c");
        }catch (IOException | InvalidYesSyntaxException err){
            err.printStackTrace();
        }
    }
}
