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
import java.util.List;

public class DefaultGenerators {
    public static void writeArguments(List<Argument> arguments, FileOutputStream out) throws IOException, InvalidYesSyntaxException{
        for (Argument argument : arguments) {
            if (argument.getClass().toString().equals(Value.class.toString()))
                putValueToCr((Value) argument, out);
            else if (argument.getClass().toString().equals(Array.class.toString()))
                putArrayToCr((Array) argument, out);
            else if (argument.getClass().toString().equals(NestedStatement.class.toString()))
               writeNestedStatement((NestedStatement) argument, out);
        }
    }

    private static void writeNestedStatement(NestedStatement argument, FileOutputStream out) throws IOException {
        if(NestedStatementParser.isSingleStatement(argument)){
            var st = (AstStatement) argument.getStack().get(0);
            StatementParser.writeStatement(st, out);
            if(argument.getType()==null||argument.getType()==Type.NULL)argument.setType(st.getType());
            if(argument.getType()!=Type.NULL){
                out.write(String.format("*(cr + ptc) = malloc(sizeof(%s));", st.getType().getCToken()).getBytes());
                out.write(String.format("*((%s *)cr[ptc]) = *((%s *)xr[ptx-1]); ptc++;", st.getType().getCToken(), st.getType().getCToken()).getBytes());
                if(PrefixTokens.CLEAR_XR.hasPrefix(argument.getPrefixes())){
                    out.write("free(xr[ptx-1]); ptx--;".getBytes());
                }
            }
        }else {
            out.write("{do{".getBytes());
            for (Argument astStatement : argument.getStack()) {
                if (!(astStatement instanceof AstStatement))
                    throw new NestedStatementException(astStatement.getLine(), "(incorrect usage of statement)");
                StatementParser.writeStatement((AstStatement) astStatement, out);
            }
            out.write("}while(0);}".getBytes());
            if(argument.getType() == Type.NULL || argument.getType() == null)
                out.write("*(cr + ptc) = malloc(0);".getBytes());
            else {
                out.write(String.format("*(cr + ptc) = malloc(sizeof(%s));", argument.getType().getCToken()).getBytes());
                out.write(String.format("*((%s *)cr[ptc]) = *((%s *)xr[ptx-1]); ptc++;", argument.getType().getCToken(), argument.getType().getCToken()).getBytes());
            }
        }

    }

    public static void putArrayToCr(Array array, FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        if(array.getToken().equals(SpecialTypeTokens.AR.getToken())){
            if(!ArgumentsArray.isInStatementDeclaration(array))
                throw new InvalidYesSyntaxException(array.getLine(), array.getToken()+" could be called only in _STATEMENT body");
        }
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
    public static String getToken(Value value){
        if(value.getToken().equals("true"))return "1";
        if(value.getToken().equals("false"))return "0";
        if(value.getType()==Type.STR)return Type.StrToCString(value.getToken());
        if(value.getToken().equals(SpecialTypeTokens.PTA.getToken())) return "*pta";
        if(value.getToken().equals(SpecialTypeTokens.PTP.getToken())) return "*ptp";
        return value.getToken();
    }
    public static void putValueToCr(Value value, FileOutputStream out) throws IOException {
        value.setType(ValueParser.getValueType(value));
        out.write(String.format("*(cr + ptc) = malloc(sizeof(%s));", value.getType().getCToken()).getBytes());
        out.write(String.format("*((%s *)cr[ptc]) = %s; ptc++;", value.getType().getCToken(),  getToken(value)).getBytes());
    }
}
