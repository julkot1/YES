import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.ast.models.NestedStatement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static pl.julkot1.yes.lexer.Lexer.resolve;
import static pl.julkot1.yes.lexer.Lexer.simplify;

public class CustomStatementTests {
    @SneakyThrows
    @Test
    void parentTest(){
        var tokens = simplify(resolve("_STATEMENT SUM {ADD 5 6;};SUM;DO {SUM};"));
        var ast = AST.build(tokens);
        assertEquals(ast.getStatementList().size(), 3);
        assertNull(ast.getStatementList().get(1).getParent());
    }
}
