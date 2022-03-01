package pl.julkot1.yes.exception;

import pl.julkot1.Main;

public class NestedStatementException extends RuntimeException {
    public NestedStatementException(long line, String err) {
        super(String.format("%s\nline %d: Nested statement error %s", Main.file.getPath(), line, err));
    }
}
