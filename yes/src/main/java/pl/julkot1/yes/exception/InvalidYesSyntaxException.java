package pl.julkot1.yes.exception;

import pl.julkot1.Main;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.lexer.tokens.Token;

public class InvalidYesSyntaxException extends Exception{
    public InvalidYesSyntaxException(){
        super("YES exception");
    }
    public InvalidYesSyntaxException(long line, String message){
        super(String.format("\n%s:line %d: %s", Main.file.getPath(), line, message));
    }
    public InvalidYesSyntaxException(Argument argument, ErrorCodes code){
        super(String.format("\n%s:line %d: %s \n error code: %s", Main.file.getPath(),argument.getLine(), argument.getToken(), code.name()));
    }
    public InvalidYesSyntaxException(Token token, ErrorCodes code){
        super(String.format("\n%s:line %d: %s \n error code: %s", Main.file.getPath(),token.line(),token, code.name()));
    }
}
