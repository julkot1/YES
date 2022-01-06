package pl.julkot1.yes.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
}
