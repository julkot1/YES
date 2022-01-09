package pl.julkot1.yes.exception;

public class InvalidYesSyntaxException extends Exception{
    public InvalidYesSyntaxException(){
        super("YES exception");
    }
    public InvalidYesSyntaxException(long line, String message){
        super(String.format("line %d: %s", line, message));
    }
}
