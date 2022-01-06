package pl.julkot1.yes.exception;

public class UndefinedStatement extends RuntimeException{
    public UndefinedStatement(long line, String token){
        super(String.format("line %d: Undefined statement %s", line, token));
    }
}

