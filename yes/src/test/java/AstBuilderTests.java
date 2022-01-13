import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
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
}
