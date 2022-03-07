import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import pl.julkot1.yes.project.File;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
public class NamespaceTests {
    @Test
    @SneakyThrows
    public void assignNamespace(){
        var rawMetadata = new ArrayList<String[]>();
        rawMetadata.add(new String[]{"import", "stdLib.yes"});
        File file = new File("std:ECHO \"gdf\";", rawMetadata);
        var ast = file.getAst();
        assertEquals(1, ast.getStatementList().size());
        assertEquals("std", ast.getStatementList().get(0).getNamespace());
    }
}
