package pl.julkot1.yes.statement.custom.interfaces;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.julkot1.yes.statement.StatementTokens;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum InterfaceStatements {
    ARG(StatementTokens.ARG.getToken(), ( Class<? extends Arg> )StatementTokens.ARG.getClazz());
    String token;
    Class<? extends Arg> clazz;
    public static boolean contains(String t){
        List<InterfaceStatements> list = new ArrayList<>(EnumSet.allOf(InterfaceStatements.class));
        return list.stream().anyMatch(s->s.token.equals(t));
    }
    public static Optional<InterfaceStatements> getByToken(String token){
        List<InterfaceStatements> list = new ArrayList<>(EnumSet.allOf(InterfaceStatements.class));
        return list.stream().filter((s)->s.token.equals(token)).findAny();
    }
}
