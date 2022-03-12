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
    AR(null, "ar"),
    RESULT(null, "rx"),
    FALSE(Type.BOOL, "false"),
    TRUE(Type.BOOL, "true"),
    PTG(Type.SIZE, "ptg"),
    PTA(Type.SIZE, "pta");
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
        List<SpecialTypeTokens> list = new ArrayList<>();
        list.add(AR);
        list.add(GR);
        return list;
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
