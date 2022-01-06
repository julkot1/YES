package pl.julkot1.yes.exception;

public class InvalidArgumentsQuantity extends RuntimeException{
    public InvalidArgumentsQuantity(long line, String token){
        super(String.format("line %d: Invalid arguments quantity in statement %s", line, token));
    }
}
