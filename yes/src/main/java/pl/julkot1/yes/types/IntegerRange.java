package pl.julkot1.yes.types;

import java.math.BigInteger;

public class IntegerRange {
    public static int CHAR_MIN = 0, CHAR_MAX = 255;
    public static int SHORT_MIN = -32768, SHORT_MAX = 32767;
    public static long INT_MIN = -2147483648, INT_MAX = 2147483647;
    public static BigInteger LONG_MIN = new BigInteger("-9223372036854775808"), LONG_MAX= new BigInteger("9223372036854775807");
    public static BigInteger SIZE_MIN = new BigInteger("0"), SIZE_MAX= new BigInteger("2147483647");
}
