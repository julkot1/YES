package pl.julkot1.yes.types;

import java.math.BigInteger;

public class IntegerRange {
    public static int CHAR_MIN = 0, CHAR_MAX = 255;
    public static int SHORT_MIN = -32768, SHORT_MAX = 32767;
    public static long INT_MIN = -2147483648, INT_MAX = 2147483647;
    public static BigInteger LONG_MIN = new BigInteger("-9223372036854775808"), LONG_MAX= new BigInteger("9223372036854775807");
    public static BigInteger SIZE_MIN = new BigInteger("0"), SIZE_MAX= new BigInteger("2147483647");

    public static boolean isChar(Number number){
        return number.longValue() >= CHAR_MIN && number.longValue() <=CHAR_MAX;
    }
    public static boolean isLong(Number number){
        return number.longValue() >= LONG_MIN.longValue() && number.longValue() <=LONG_MAX.longValue();
    }
    public static boolean isShort(Number number){
        return number.longValue() >= SHORT_MIN && number.longValue() <=SHORT_MAX;
    }
    public static boolean isInt(Number number){
        return number.longValue() >= INT_MIN && number.longValue() <=INT_MAX;
    }
    public static boolean isSize(Number number){
        return number.longValue() >= SIZE_MIN.longValue() && number.longValue() <=SIZE_MAX.longValue();
    }

}
