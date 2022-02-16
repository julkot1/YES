import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
import pl.julkot1.yes.lexer.tokens.SpecialTypeTokens;
import pl.julkot1.yes.types.Type;

import static pl.julkot1.yes.lexer.Lexer.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AstBuilderTests {

    @SneakyThrows
    @Test
    void statementBuildTest(){
        var tokens = simplify(resolve("ADD 1 2;SUB 35 43;DO 5;"));
        var ast = AST.build(tokens);
        assertEquals(ast.getStatementList().size(), 3);
        assertEquals(ast.getStatementList().get(0).getToken(), "ADD");
        assertEquals(ast.getStatementList().get(1).getToken(), "SUB");
    }

    @SneakyThrows
    @Test
    void statementBuildWithPrefixAndTypeTest(){
        var tokens = simplify(resolve("(Int) $ADD 21 12;"));
        var ast = AST.build(tokens);
        assertEquals(1, ast.getStatementList().size());
        var add = ast.getStatementList().get(0);
        assertEquals("ADD", add.getToken());
        assertEquals(Type.INT, add.getType());
        assertEquals(1, add.getPrefixes().size());
        assertEquals(PrefixTokens.CALL_PARENT_CR, add.getPrefixes().get(0));
    }

    @SneakyThrows
    @Test
    void statementBuildValuesTest(){
        var tokens = simplify(resolve("ADD 21 (Short) 54 \"fd\";"));
        var ast = AST.build(tokens);
        assertEquals(1, ast.getStatementList().size());
        var add = ast.getStatementList().get(0);
        assertEquals("ADD", add.getToken());
        assertEquals(3, add.getArguments().size());

        var arg1 = add.getArguments().get(0);
        assertEquals("21", arg1.getToken());

        var arg2 = add.getArguments().get(1);
        assertEquals("54", arg2.getToken());
        assertEquals(Type.SHORT, arg2.getType());

        var arg3 = add.getArguments().get(2);
        assertEquals("\"fd\"", arg3.getToken());

    }

    @SneakyThrows
    @Test
    void statementBuildWithSpecialTest(){
        var tokens = simplify(resolve("ADD true ptx;"));
        var ast = AST.build(tokens);
        assertEquals(1, ast.getStatementList().size());
        var add = ast.getStatementList().get(0);
        assertEquals("ADD", add.getToken());
        assertEquals(2, add.getArguments().size());
        assertEquals(SpecialTypeTokens.TRUE.getToken(), add.getArguments().get(0).getToken());
    }

    @SneakyThrows
    @Test
    void statementBuildWithArray(){
        var tokens = simplify(resolve("ADD (Int) 8 6;"));
        var ast = AST.build(tokens);
        assertEquals(1, ast.getStatementList().size());
        var add = ast.getStatementList().get(0);
        /*
        assertEquals("ADD", add.getToken());
        assertEquals(2, add.getArguments().size());
        assertEquals(Type.INT, ((Array)add.getArguments().get(0)).getType());
        assertEquals("0", ((Array)add.getArguments().get(0)).getIndex().getToken());
        assertEquals("6", add.getArguments().get(1).getToken());

         */

    }

    @SneakyThrows
    @Test
    void statementBuildWithArrayIndex(){
        var tokens = simplify(resolve("ADD (Int) y[3] 6 (Char) gr[ptg];"));
        var ast = AST.build(tokens);
        assertEquals(1, ast.getStatementList().size());
        var add = ast.getStatementList().get(0);
        /*
        assertEquals("ADD", add.getToken());
        assertEquals(3, add.getArguments().size());
        assertEquals(Type.INT, ((Array)add.getArguments().get(0)).getType());
        assertEquals("3", ((Array)add.getArguments().get(0)).getIndex().getToken());
        assertEquals("6", add.getArguments().get(1).getToken());
        assertEquals(SpecialTypeTokens.GR.getToken(), add.getArguments().get(2).getToken());
        assertEquals(Type.CHAR, add.getArguments().get(2).getType());
        assertEquals("ptg", ((Array)add.getArguments().get(2)).getIndex().getToken());

         */

    }
    @SneakyThrows
    @Test
    void statementBuildWithNested(){
        var tokens = simplify(resolve("DO {ADD 3 6; ECHO \"sdd\"} 6;"));
        var ast = AST.build(tokens);
        assertEquals(1, ast.getStatementList().size());
        var DO = ast.getStatementList().get(0);
        /*assertEquals("DO", DO.getToken());
        assertEquals(2, DO.getArguments().size());
        assertEquals(NestedStatement.class, DO.getArguments().get(0).getClass());
        assertEquals(2, ((NestedStatement)DO.getArguments().get(0)).getStack().size());
        assertEquals("ADD", ((NestedStatement)DO.getArguments().get(0)).getStack().get(0).getToken());
        assertEquals("3", ((AstStatement)((NestedStatement)DO.getArguments().get(0)).getStack().get(0)).getArguments().get(0).getToken());
        assertEquals("6",  ((AstStatement)((NestedStatement)DO.getArguments().get(0)).getStack().get(0)).getArguments().get(1).getToken());
        assertEquals("ECHO", ((NestedStatement)DO.getArguments().get(0)).getStack().get(1).getToken());
        assertEquals("\"sdd\"",  ((AstStatement)((NestedStatement)DO.getArguments().get(0)).getStack().get(1)).getArguments().get(0).getToken());
    */
    }

}
