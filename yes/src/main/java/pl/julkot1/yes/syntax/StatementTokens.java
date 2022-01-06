package pl.julkot1.yes.syntax;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.math.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum StatementTokens {
    ADD("ADD", AddStatement.class),
    SUB("SUB", SubStatement.class),
    DIV("DIV", DivStatement.class),
    MUL("MUL", MulStatement.class),
    BOR("bOR", BorStatement.class),
    XOR("XOR",XorStatement.class),
    LSHIFT("lSHIFT", LshiftStatement.class),
    RSHIFT("rSHIFT", RshiftStatement.class),
    BAND("bAND", BandStatement.class),
    BNOT("bNOT", BnotStatement.class),
    MOD("MOD", ModStatement.class);
    public static Optional<StatementTokens> getByToken(String token){
        List<StatementTokens> list = new ArrayList<>(EnumSet.allOf(StatementTokens.class));
        return list.stream().filter((s)->s.token.equals(token)).findAny();
    }

    String token;
    Class<? extends Statement> clazz;
}
