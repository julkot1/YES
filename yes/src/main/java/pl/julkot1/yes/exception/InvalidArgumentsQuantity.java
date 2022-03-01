package pl.julkot1.yes.exception;

import pl.julkot1.Main;

public class InvalidArgumentsQuantity extends RuntimeException{
    public InvalidArgumentsQuantity(long line, String token){
        super(String.format("\n%s:line %d: Invalid arguments quantity in statement %s", Main.file.getPath(), line, token));
    }
    public InvalidArgumentsQuantity(long line, String token, int size, int expectedSize, boolean min){
        super(String.format("\n%s:line %d: Invalid arguments quantity in statement %s (given %d, expected%s%d)", Main.file.getPath(),
                line, token, size, (min?" min ": " "), expectedSize));
    }
}
