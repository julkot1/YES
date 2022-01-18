package pl.julkot1.yes.exception;

public class InvalidPrefixUsageException extends RuntimeException {
    public InvalidPrefixUsageException(long line, String token, String prefix) {
        super(String.format("line %d: Invalid %s prefix usage in %s", line, token, prefix));
    }
}