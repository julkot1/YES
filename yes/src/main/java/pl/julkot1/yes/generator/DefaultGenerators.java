package pl.julkot1.yes.generator;

import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.parser.ArrayParser;
import pl.julkot1.yes.generator.parser.StatementParser;
import pl.julkot1.yes.lexer.PrefixTokens;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.generator.parser.ValueParser;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DefaultGenerators {
    public static void writeArguments(List<Argument> arguments, FileOutputStream out) throws IOException{
        for (Argument argument : arguments) {
            if (argument.getClass().toString().equals(AstStatement.class.toString())) {
                System.out.println("TODO");
            } else if (argument.getClass().toString().equals(Value.class.toString()))
                putValueToCr((Value) argument, out);
            else if (argument.getClass().toString().equals(Array.class.toString()))
                putArrayToCr((Array) argument, out);
        }
    }
    public static void putArrayToCr(Array array, FileOutputStream out) throws IOException {
        if(array.getIndex() instanceof Value){
            if(!ValueParser.castToSize((Value) array.getIndex())) throw  new TypeException(array.getLine(),array.getToken(),"array index must be Size type");
            if(array.getType() == Type.NULL) throw  new TypeException(array.getLine(),array.getToken(),"to take an element from the array it's type must be given");
        }
        if(array.getIndex() instanceof AstStatement){
            StatementParser.writeStatement((AstStatement) array.getIndex(), out);
        }
        var type = array.getType().getCToken();
        var element =  ArrayParser.getElement(array);
        if (PrefixTokens.REFERENCE.hasPrefix(array.getPrefixes())){
            out.write(String.format("cr[ptc] = %s; ptc++;", element).getBytes());
        }
        else{
            out.write(String.format("*(cr + ptc) = malloc(sizeof(%s));", type).getBytes());
            out.write(String.format("*((%s *)cr[ptc]) = %s; ptc++;", type, element).getBytes());
        }

    }
    public static void putValueToCr(Value value, FileOutputStream out) throws IOException {
        value.setType(ValueParser.getValueType(value));
        out.write(String.format("*(cr + ptc) = malloc(sizeof(%s));", value.getType().getCToken()).getBytes());
        out.write(String.format("*((%s *)cr[ptc]) = %s; ptc++;", value.getType().getCToken(),  value.getToken()).getBytes());
    }
}
