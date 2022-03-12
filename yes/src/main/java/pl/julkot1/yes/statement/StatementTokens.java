package pl.julkot1.yes.statement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.julkot1.yes.statement.array.*;
import pl.julkot1.yes.statement.conditional.IfStatement;
import pl.julkot1.yes.statement.conditional.RepeatStatement;
import pl.julkot1.yes.statement.conditional.YellStatement;
import pl.julkot1.yes.statement.custom.StatementDeclaration;
import pl.julkot1.yes.statement.custom.interfaces.Arg;
import pl.julkot1.yes.statement.custom.interfaces.Interface;
import pl.julkot1.yes.statement.io.CallStatement;
import pl.julkot1.yes.statement.io.InStatement;
import pl.julkot1.yes.statement.logic.*;
import pl.julkot1.yes.statement.math.*;
import pl.julkot1.yes.statement.other.*;
import pl.julkot1.yes.statement.str.CharAtStatement;
import pl.julkot1.yes.statement.str.FormatStatement;
import pl.julkot1.yes.statement.str.LenStatement;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum StatementTokens {
    ADD("ADD", AddStatement.class),
    SUB("SUB", SubStatement.class),
    DIV("DIV", DivStatement.class),
    MUL("MUL", MulStatement.class),
    B_OR("bOR", BorStatement.class),
    XOR("XOR",XorStatement.class),
    L_SHIFT("lSHIFT", LshiftStatement.class),
    R_SHIFT("rSHIFT", RshiftStatement.class),
    B_AND("bAND", BandStatement.class),
    B_NOT("bNOT", BnotStatement.class),
    MOD("MOD", ModStatement.class),
    EQ("EQ", EqStatement.class),
    GT("GT", GtStatement.class),
    LT("LT", LtStatement.class),
    AND("AND", AndStatement.class),
    OR("OR", OrStatement.class),
    NOT("NOT", NotStatement.class),
    N_EQ("nEQ", NEqStatement.class),
    E_LT("eLT", ELtStatement.class),
    E_GT("eGT", EGtStatement.class),
    PUSH("PUSH", PushStatement.class),
    REPLACE("REPLACE", ReplaceStatement.class),
    IN("IN", InStatement.class),
    IF("IF", IfStatement.class),
    REPEAT("REPEAT", RepeatStatement.class),
    YELL("YELL", YellStatement.class),
    RT("RT", RtStatement.class),
    SYSCALL("SYSCALL", Syscall.class),
    STATEMENT_DEF("_STATEMENT", StatementDeclaration.class),
    INTERFACE_DEF("_INTERFACE", Interface.class),
    ARG("ARG", Arg.class),
    STR_LEN("LEN", LenStatement.class),
    STR_FORMAT("FORMAT", FormatStatement.class),
    CHAR_AT("CHAR", CharAtStatement.class),
    BREAK("END", BreakStatement.class),
    NEXT("NEXT", NextStatement.class),
    AS("AS", AsStatement.class),
    DO("DO", DoStatement.class),
    MATCH("MATCH", MatchStatement.class),
    POP("POP", PopStatement.class),
    CALL ("CALL", CallStatement.class);


    public static Optional<StatementTokens> getByToken(String token){
        List<StatementTokens> list = new ArrayList<>(EnumSet.allOf(StatementTokens.class));
        return list.stream().filter((s)->s.token.equals(token)).findAny();
    }

    final String token;
    final Class<? extends Statement> clazz;



}
