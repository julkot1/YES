package pl.julkot1.yes.ast.builder;

import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.Index;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.tokens.SpecialTypeTokens;
import pl.julkot1.yes.lexer.tokens.SyntaxTokens;
import pl.julkot1.yes.lexer.tokens.Token;
import pl.julkot1.yes.lexer.tokens.TokenType;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IndexBuilder {
    public static int parseIndex(Array array, int i, List<Token> list) throws InvalidYesSyntaxException {
        var indexTokens = getIndex(list, i);
        if(indexTokens == null ){
            array.setIndex(new Index(array.getLine(), new Value(Type.SIZE, "0", array.getLine(), array)));
            return 0;
        }else{
            if(indexTokens.size()==1){
                array.setIndex(new Index(array.getLine(), new Value(Type.SIZE, "0", array.getLine(), array)));
            }
            if(indexTokens.size()==3) {
                var first = indexTokens.get(1);
                if (first.type() == TokenType.TYPE) throw new InvalidYesSyntaxException(first.line(), "Casting in array's index is not allowed");
                if(SpecialTypeTokens.isArray(first.obj().toString()))
                    array.setIndex(new Index(array.getLine(), buildIndexArray(first, array, new Value(Type.SIZE, "1", array.getLine(), array))));
                else array.setIndex(new Index(array.getLine(), new Value(Type.SIZE, first.obj().toString(), array.getLine(), array)));
            }
            else throw new InvalidYesSyntaxException(array.getLine(), "Invalid array index declaration");

        }
        return indexTokens.size();
    }


    private static Array buildIndexArray(Token t, Array parent, Argument index){
        var array = new Array(Type.SIZE, t.obj().toString(), t.line(), parent);
        array.setIndex(new Index(index.getLine(), index));
        return array;
    }
    private static List<Token> getIndex(List<Token> list, int i) {
        var count = 0;
        var newList = new ArrayList<Token>();
        for (int j = i+1; j < list.size(); j++) {
            var token = list.get(j);
            if(j == i){
                if(token.obj().equals(SyntaxTokens.REGISTER_OPEN))count++;
                else return null;
            }else {
                if(token.obj().equals(SyntaxTokens.REGISTER_OPEN))count++;
                else if(token.obj().equals(SyntaxTokens.REGISTER_END))count--;
                newList.add(token);
                if(count==0)return newList;
            }
        }
        return null;
    }

}
