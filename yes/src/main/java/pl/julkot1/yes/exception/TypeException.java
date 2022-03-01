package pl.julkot1.yes.exception;

import pl.julkot1.Main;

public class TypeException extends RuntimeException {
    public TypeException(long line, String token, String err) {
        super(String.format("\n%s:line %d: Type error in %s (%s)", Main.file.getPath(), line, token, err));
    }
    public TypeException(long line, String token, String type, String expected) {
        super(String.format("\n%s:line %d: Type error in %s (given %s expected %s)", Main.file.getPath(), line, token, type, expected));
    }
}
