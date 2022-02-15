package pl.julkot1.yes.lexer.tokens;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.julkot1.yes.types.Type;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum SpecialTypeTokens {
    GR(null, "gr"),
    PR(null, "pr"),
    CR(null, "cr"),
    XR(null, "xr"),
    AR(null, "ar"),
    FALSE(Type.BOOL, "false"),
    TRUE(Type.BOOL, "true"),
    PTG(Type.SIZE, "ptg"),
    PTX(Type.SIZE, "ptx"),
    PTC(Type.SIZE, "ptc"),
    PTA(Type.SIZE, "pta"),
    PTP(Type.SIZE, "ptp");
    final Type forType;
    final String token;

    public static List<SpecialTypeTokens> getPointers(){
        List<SpecialTypeTokens> list = new ArrayList<>(EnumSet.allOf(SpecialTypeTokens.class));
        return list.stream()
                .filter((r)-> r.forType == Type.SIZE)
                .collect(Collectors.toList());
    }
    public boolean isBool(){
        return this == SpecialTypeTokens.TRUE || this == SpecialTypeTokens.FALSE;
    }
    public boolean isPointer(){
        for (SpecialTypeTokens pointer : SpecialTypeTokens.getPointers()) {
            if(pointer == this)return true;
        }
        return false;
    }
    public static List<SpecialTypeTokens> getArrays(){
        List<SpecialTypeTokens> list = new ArrayList<>(EnumSet.allOf(SpecialTypeTokens.class));
        return list.stream()
                .filter((r)-> r.forType == null)
                .collect(Collectors.toList());
    }
    public static boolean isArray(String token){
        var t = SpecialTypeTokens.getToken(token);
        if(t.isPresent()){
            for (SpecialTypeTokens el : getArrays())
                if(el == t.get())return true;
        }
        return false;
    }
    public static boolean isPointer(String token){
        var t = SpecialTypeTokens.getToken(token);
        if(t.isPresent()){
            for (SpecialTypeTokens el : getPointers())
                if(el == t.get())return true;
        }
        return false;
    }
    public static Optional<SpecialTypeTokens> getToken(String token){
        var list = new ArrayList<>(EnumSet.allOf(SpecialTypeTokens.class));
        return list.stream().filter((s)-> Objects.equals(s.token, token)).findAny();
    }
}
