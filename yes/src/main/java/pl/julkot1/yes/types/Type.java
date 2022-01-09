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
    BOOL("Bool", "bool", "%b", "%hhu"),
    SIZE("Size", "unsigned long", "%p", "%lu"),
    INT("Int", "int", "%i", "%i"),
    NULL("None", null, null, null);
    String yesToken, cToken, yesFormatSpecifier, cFormatSpecifier;

    public static Optional<Type> getTypeByYToken(String yesToken){
        var list = new ArrayList<>(EnumSet.allOf(Type.class));
        return list.stream().filter((s)->s.yesToken.equals(yesToken)).findAny();

    }
}
