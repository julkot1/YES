package pl.julkot1.yes.generator.parser;

import org.apache.commons.lang3.math.NumberUtils;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.lexer.tokens.SpecialTypeTokens;
import pl.julkot1.yes.types.Type;

import static pl.julkot1.yes.types.IntegerRange.*;
import static pl.julkot1.yes.types.IntegerRange.SHORT_MAX;

public class ValueParser {
    private static boolean isBool(String token){
        return token.equals(SpecialTypeTokens.FALSE.getToken()) ||
                token.equals(SpecialTypeTokens.TRUE.getToken());
    }
    private static boolean isFloat(String token){
        if(NumberUtils.isCreatable(token)){
            return NumberUtils.createNumber(token).getClass().getName().equals("java.lang.Float");
        }return false;
    }

    private static boolean isChar(int value){
        return value >= CHAR_MIN && value <= CHAR_MAX;
    }
    private static boolean isShort(int value){
        return value >= SHORT_MIN && value <= SHORT_MAX;
    }
    private static boolean isInt(long value){
        return value >= INT_MIN && value <= INT_MAX;
    }

    private static Type isInteger(String token){
        if(NumberUtils.isCreatable(token)){
            var number = NumberUtils.createNumber(token);
            if(number.getClass().getName().equals( "java.lang.Integer")){
                if(isChar(number.intValue()))
                    return Type.CHAR;
                if(isShort(number.intValue()))
                    return Type.SHORT;
                if(isInt(number.longValue()))
                    return Type.INT;
            }
            if(number.getClass().getName().equals( "java.lang.Long"))return Type.LONG;
        }return Type.NULL;
    }
    private static boolean isPointer(String token){
        return  SpecialTypeTokens.getPointers().stream()
                .anyMatch((s)->s.getToken().equals(token));
    }
    public static Type getValueType(Value value){
        if (value.getType() != Type.NULL)
            return value.getType();
        if(isBool(value.getToken()))
            return Type.BOOL;
        if(isFloat(value.getToken()))
            return Type.FLOAT;
        if(isPointer(value.getToken()))
            return Type.SIZE;
        return  isInteger(value.getToken());
    }
    public static boolean castToSize(Value value){
        return isInteger(value.getToken()) != Type.NULL || value.getType() == Type.SIZE;
    }
}
