package pl.julkot1.yes.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum Type {
    STR("Str", "char *", "%S", "%s"),
    FLOAT("Float", "float", "%f", "%f"),
    LONG("Long", "long  long", "%l", "%lld"),
    SHORT("Short", "short", "%s", "%hi"),
    CHAR("Char", "unsigned char", "%c", "%hhu"),
    BOOL("Bool", "unsigned char", "%b", "%hhu"),
    SIZE("Size", "unsigned long", "%p", "%lu"),
    INT("Int", "int", "%i", "%i"),
    VOID("Void", "void *", "%v", "%p"),
    NULL("None", null, null, null);
    String yesToken, cToken, yesFormatSpecifier, cFormatSpecifier;

    public static Optional<Type> getTypeByYToken(String yesToken){
        var list = new ArrayList<>(EnumSet.allOf(Type.class));
        return list.stream().filter((s)->s.yesToken.equals(yesToken)).findAny();

    }
    public static String StrToCString(String value){
        return value
                .replace(LONG.yesFormatSpecifier, LONG.cFormatSpecifier)
                .replace(VOID.yesFormatSpecifier, VOID.cFormatSpecifier)
                .replace(SIZE.yesFormatSpecifier, SIZE.cFormatSpecifier)
                .replace(SHORT.yesFormatSpecifier, SHORT.cFormatSpecifier)
                .replace(CHAR.yesFormatSpecifier, CHAR.cFormatSpecifier)
                .replace(STR.yesFormatSpecifier, STR.cFormatSpecifier)
                .replace(BOOL.yesFormatSpecifier, BOOL.cFormatSpecifier);
    }
}
