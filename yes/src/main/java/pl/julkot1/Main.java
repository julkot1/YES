package pl.julkot1;

import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.generator.CGenerator;
import pl.julkot1.yes.lexer.Lexer;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {


        try{
            var t = Lexer.resolveFile("program.yes");
            var a = AST.build(t);
            System.out.println(a);
            CGenerator.generate(a, "out.c");
        }catch (IOException  err){
            err.printStackTrace();
        }
    }
}
