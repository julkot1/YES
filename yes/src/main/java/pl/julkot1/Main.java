package pl.julkot1;

import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.generator.CGenerator;
import pl.julkot1.yes.types.Type;

import java.io.IOException;


public class Main {
    public static void main(String[] args) {

       var ast = new AST();
       var s = new AstStatement("ADD",1, null);
       //1
       s.addArgument(new Value(Type.NULL, "36", s.getLine(), s));
       //2
       var b = new NestedStatement(Type.NULL, s.getLine(), s);
       var add = new AstStatement("ADD",1, b);
       add.addArgument(new Value(Type.NULL, "10", s.getLine(), s));
       add.addArgument(new Value(Type.NULL, "5", s.getLine(), s));
       b.addToStack(add);

       s.addArgument(b);
       ast.getStatementList().add(s);

        try{
            CGenerator.generate(ast, "out.c");
        }catch (IOException  err){
            err.printStackTrace();
        }
    }
}
