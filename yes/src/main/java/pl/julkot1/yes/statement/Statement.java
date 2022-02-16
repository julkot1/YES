package pl.julkot1.yes.statement;

import lombok.Getter;
import lombok.Setter;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;


public abstract class Statement {
    public Statement(AstStatement astStatement){
        this.astStatement = astStatement;
        arguments = new ArrayList<>();
    }

    protected List<String> arguments;
    @Getter @Setter
    private String returning;
    protected abstract void validArguments() throws InvalidYesSyntaxException;
    protected abstract void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException;
    protected abstract List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException;
    public AstStatement astStatement;
    public void generate(FileOutputStream out, boolean writeOut) throws IOException, InvalidYesSyntaxException {
        validArguments();
        applyPrefixes(out);
        arguments=writeArguments(out);
        setReturning();
        if(writeOut){
            write(out);
        }
    }
    protected void setReturning() throws InvalidYesSyntaxException {}
    protected  void applyPrefixes(FileOutputStream out)throws IOException{

    }
}
