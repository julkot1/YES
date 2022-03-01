package pl.julkot1.yes.exception;

import pl.julkot1.Main;

public class InvalidArrayIndexException extends RuntimeException {
    public InvalidArrayIndexException(long line, String token, String err) {
        super(String.format("\n%s:line %d: Invalid index in %s array (%s)", Main.file.getPath(), line, token, err));
    }
}