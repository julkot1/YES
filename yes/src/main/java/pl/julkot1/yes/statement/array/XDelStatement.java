package pl.julkot1.yes.statement.array;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.Statement;

import java.io.FileOutputStream;
import java.io.IOException;

public class XDelStatement extends GDelStatement{
    public XDelStatement(AstStatement astStatement) {
        super(astStatement);
    }


    @Override
    protected void write(FileOutputStream out) throws IOException {
        out.write("if(*((unsigned long*)cr[0]) <= ptx){ptx-=*((unsigned long*)cr[0]); for (int i = 0; i < *((unsigned long *)cr[0]); i++)free(xr[ptx -i]);}".getBytes());
    }

}
