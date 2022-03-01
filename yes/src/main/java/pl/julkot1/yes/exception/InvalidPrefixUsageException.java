package pl.julkot1.yes.exception;

import pl.julkot1.Main;

public class InvalidPrefixUsageException extends RuntimeException {
    public InvalidPrefixUsageException(long line, String token, String prefix) {
        super(String.format("\n%s:line %d: Invalid %s prefix usage in %s", Main.file.getPath(), line, token, prefix));
    }
}