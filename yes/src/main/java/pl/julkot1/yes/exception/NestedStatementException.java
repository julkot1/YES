package pl.julkot1.yes.exception;

public class NestedStatementException extends RuntimeException {
    public NestedStatementException(long line, String err) {
        super(String.format("line %d: Nested statement error %s", line, err));
    }
}
