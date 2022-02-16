package pl.julkot1.yes.statement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.julkot1.yes.statement.array.*;
import pl.julkot1.yes.statement.conditional.*;
import pl.julkot1.yes.statement.custom.interfaces.Interface;
import pl.julkot1.yes.statement.custom.StatementDeclaration;
import pl.julkot1.yes.statement.custom.interfaces.Arg;
import pl.julkot1.yes.statement.io.*;
import pl.julkot1.yes.statement.logic.*;
import pl.julkot1.yes.statement.math.*;
import pl.julkot1.yes.statement.other.*;

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
    B_OR("bOR", BorStatement.class),
    XOR("XOR",XorStatement.class),
    L_SHIFT("lSHIFT", LshiftStatement.class),
    R_SHIFT("rSHIFT", RshiftStatement.class),
    B_AND("bAND", BandStatement.class),
    B_NOT("bNOT", BnotStatement.class),
    MOD("MOD", ModStatement.class),
    EQ("EQ", EqStatement.class),
    GT("GT", GtStatement.class),
    LW("Lt", LtStatement.class),
    AND("AND", AndStatement.class),
    OR("OR", OrStatement.class),
    NOT("NOT", NotStatement.class),
    N_EQ("nEQ", NEqStatement.class),
    E_LT("eLT", ELtStatement.class),
    E_GT("eGT", EGtStatement.class),
    DO("DO", DoStatement.class),
    G_DELL("gDEL", GDelStatement.class),
    PUSH("PUSH", PushStatement.class),
    REPLACE("REPLACE", ReplaceStatement.class),
    SWAP("SWAP", SwapStatement.class),
    ECHO("ECHO", EchoStatement.class),
    IN("IN", InStatement.class),
    IF("IF", IfStatement.class),
    REPEAT("REPEAT", RepeatStatement.class),
    YELL("YELL", YellStatement.class),
    RT("RT", RtStatement.class),
    SYSCALL("SYSCALL", Syscall.class),
    ALLOC("ALLOC", Alloc.class),
    STATEMENT_DEF("_STATEMENT", StatementDeclaration.class),
    INTERFACE_DEF("_INTERFACE", Interface.class),
    ARG("ARG", Arg.class),
    CALL ("CALL", InStatement.class);


    public static Optional<StatementTokens> getByToken(String token){
        List<StatementTokens> list = new ArrayList<>(EnumSet.allOf(StatementTokens.class));
        return list.stream().filter((s)->s.token.equals(token)).findAny();
    }

    String token;
    Class<? extends Statement> clazz;
}
