package pl.julkot1.yes.exception;

import pl.julkot1.yes.ast.models.Argument;

public class InvalidYesSyntaxException extends Exception{
    public InvalidYesSyntaxException(){
        super("YES exception");
    }
    public InvalidYesSyntaxException(long line, String message){
        super(String.format("line %d: %s", line, message));
    }
    public InvalidYesSyntaxException(Argument argument, ErrorCodes code){
        super(String.format("\nline %d: %s \n error code: %s", argument.getLine(), argument.getToken(), code.name()));
    }
}
