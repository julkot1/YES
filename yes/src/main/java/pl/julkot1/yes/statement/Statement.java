package pl.julkot1.yes.statement;

import lombok.AllArgsConstructor;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.PrefixTokens;

import java.io.FileOutputStream;
import java.io.IOException;

@AllArgsConstructor
public abstract class Statement {
    public static final String BEGIN = "{", END="free(cr);}";
    protected abstract void validArguments() throws InvalidYesSyntaxException;
    protected abstract void write(FileOutputStream out) throws IOException ;
    protected abstract void writeArguments(FileOutputStream out) throws IOException;
    protected AstStatement astStatement;
    public void generate(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        validArguments();
        out.write(BEGIN.getBytes());
        out.write(String.format(" void **cr = malloc(%d * sizeof(void *));unsigned long ptc =  0;", this.astStatement.getArguments().size()).getBytes());
        applyPrefixes(out);
        writeArguments(out);
        write(out);
        out.write(END.getBytes());
    }

    protected  void applyPrefixes(FileOutputStream out)throws IOException{
        if(PrefixTokens.CALL_PARENT_CR.hasPrefix(this.astStatement.getPrefixes())){
            out.write("void **pr = cr;unsigned long *ptp = &ptc;".getBytes());
        }
    }
}
