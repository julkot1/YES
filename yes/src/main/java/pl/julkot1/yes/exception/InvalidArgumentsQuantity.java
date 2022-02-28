package pl.julkot1.yes.exception;

public class InvalidArgumentsQuantity extends RuntimeException{
    public InvalidArgumentsQuantity(long line, String token){
        super(String.format("\nline %d: Invalid arguments quantity in statement %s", line, token));
    }
    public InvalidArgumentsQuantity(long line, String token, int size, int expectedSize, boolean min){
        super(String.format("\nline %d: Invalid arguments quantity in statement %s (given %d, expected%s%d)",
                line, token, size, (min?" min ": " "), expectedSize));
    }
}
