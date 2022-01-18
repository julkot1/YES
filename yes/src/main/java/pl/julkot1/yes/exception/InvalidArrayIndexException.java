package pl.julkot1.yes.exception;

public class InvalidArrayIndexException extends RuntimeException {
    public InvalidArrayIndexException(long line, String token, String err) {
        super(String.format("line %d: Invalid index in %s array (%s)", line, token, err));
    }
}