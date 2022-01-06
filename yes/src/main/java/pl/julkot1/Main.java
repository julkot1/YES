package pl.julkot1;

import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.CGenerator;
import pl.julkot1.yes.lexer.PrefixTokens;
import pl.julkot1.yes.types.Type;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class Main {
    public static void main(String[] args) {

       var ast = new AST();
       var s = new AstStatement("ADD",1);
       s.getPrefixes().add(PrefixTokens.CALL_PARENT_CR);
       s.addArgument(new Value(Type.NULL, "36", s.getLine()));
       var index = new AstStatement("ADD", 1);
       index.getArguments().add(new Value(Type.INT, "1",1));
       index.getArguments().add(new Value(Type.INT, "2",1));
       var ar = new Array(Type.INT, "gr", index,s.getLine());
       ar.getPrefixes().add(PrefixTokens.REFERENCE);
       s.addArgument(ar);
       ast.getStatementList().add(s);

        try{
            CGenerator.generate(ast, "out.c");
        }catch (IOException  err){
            err.printStackTrace();
        }
    }
}
