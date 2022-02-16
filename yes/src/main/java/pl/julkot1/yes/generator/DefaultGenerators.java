package pl.julkot1.yes.generator;

import pl.julkot1.yes.ast.models.*;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.NestedStatementException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.parser.ArrayParser;
import pl.julkot1.yes.generator.parser.NestedStatementParser;
import pl.julkot1.yes.generator.parser.StatementParser;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
import pl.julkot1.yes.lexer.tokens.SpecialTypeTokens;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.generator.parser.ValueParser;
import pl.julkot1.yes.util.ArgumentsArray;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefaultGenerators {
    public static List<String> writeArguments(List<Argument> arguments, FileOutputStream out) throws IOException, InvalidYesSyntaxException{
        List<String> args = new ArrayList<>();
        for (Argument argument : arguments) {
            if (argument.getClass().toString().equals(Value.class.toString()))
                args.add(putValueToCr((Value) argument));
            else if (argument.getClass().toString().equals(Array.class.toString()))
               args.add( putArrayToCr((Array) argument, out));
            else if (argument.getClass().toString().equals(NestedStatement.class.toString())) {
                args.add(writeNestedStatement((NestedStatement) argument, out));
            }
        }
        return args;
    }

    private static String writeNestedStatement(NestedStatement argument, FileOutputStream out) throws IOException {
        if(NestedStatementParser.isSingleStatement(argument)){
            var st = (AstStatement) argument.getStack().get(0);
            var ret = StatementParser.writeStatement(st, out, false);
            if(argument.getType()==null||argument.getType()==Type.NULL)argument.setType(st.getType());
            return ret;
        }else {

            for (Argument astStatement : argument.getStack()) {
                if (!(astStatement instanceof AstStatement))
                    throw new NestedStatementException(astStatement.getLine(), "(incorrect usage of statement)");
                StatementParser.writeStatement((AstStatement) astStatement, out, true);
            }


        }
        return null;

    }

    public static String putArrayToCr(Array array, FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        if(array.getToken().equals(SpecialTypeTokens.AR.getToken())){
            if(!ArgumentsArray.isInStatementDeclaration(array))
                throw new InvalidYesSyntaxException(array.getLine(), array.getToken()+" could be called only in _STATEMENT body");
        }
        if(array.getIndex() instanceof Value){
            if(!ValueParser.castToSize((Value) array.getIndex())) throw  new TypeException(array.getLine(),array.getToken(),"array index must be Size type");
            if(array.getType() == Type.NULL) throw  new TypeException(array.getLine(),array.getToken(),"to take an element from the array it's type must be given");
        }
        if(array.getIndex() instanceof AstStatement){
            StatementParser.writeStatement((AstStatement) array.getIndex(), out, true);
        }
        var type = array.getType().getCToken();
        var element =  ArrayParser.getElement(array);
        //TODO reference
        if (PrefixTokens.REFERENCE.hasPrefix(array.getPrefixes())){
            //return element;
            //out.write(String.format("cr[ptc] = %s; ptc++;", element).getBytes());
        }
        else{
            //out.write(String.format("*(cr + ptc) = malloc(sizeof(%s));", type).getBytes());
            //out.write(String.format("*((%s *)cr[ptc]) = %s; ptc++;", type, element).getBytes());
        }
        return element;

    }
    public static String getToken(Value value){
        if(value.getToken().equals("true"))return "1";
        if(value.getToken().equals("false"))return "0";
        if(value.getType()==Type.STR)return Type.StrToCString(value.getToken());
        if(value.getToken().equals(SpecialTypeTokens.PTA.getToken())) return "*pta";
        return value.getToken();
    }
    public static String putValueToCr(Value value) {
        value.setType(ValueParser.getValueType(value));
        return getToken(value);
    }
}
