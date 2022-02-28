package pl.julkot1.yes.exception;

public class TypeException extends RuntimeException {
    public TypeException(long line, String token, String err) {
        super(String.format("line %d: Type error in %s (%s)", line, token, err));
    }
    public TypeException(long line, String token, String type, String expected) {
        super(String.format("line %d: Type error in %s (given %s expected %s)", line, token, type, expected));
    }
}
