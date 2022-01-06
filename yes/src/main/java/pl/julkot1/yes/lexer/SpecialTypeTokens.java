package pl.julkot1.yes.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum SpecialTypeTokens {
    GR(null, "gr"),
    PR(null, "pr"),
    CR(null, "cr"),
    XR(null, "xr"),
    FALSE(Type.BOOL, "false"),
    TRUE(Type.BOOL, "true"),
    PTG(Type.SIZE, "ptg"),
    PTX(Type.SIZE, "ptx"),
    PTC(Type.SIZE, "ptc");
    Type forType;
    String token;

    public static List<SpecialTypeTokens> getPointers(){
        List<SpecialTypeTokens> list = new ArrayList<>(EnumSet.allOf(SpecialTypeTokens.class));
        return list.stream()
                .filter((r)-> r.forType == Type.SIZE)
                .collect(Collectors.toList());
    }
    public static List<SpecialTypeTokens> getArrays(){
        List<SpecialTypeTokens> list = new ArrayList<>(EnumSet.allOf(SpecialTypeTokens.class));
        return list.stream()
                .filter((r)-> r.forType == null)
                .collect(Collectors.toList());
    }
}
