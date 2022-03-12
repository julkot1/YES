package pl.julkot1.yes.statement;

import lombok.Getter;
import lombok.Setter;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.NestedStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class Statement {

    public Statement(AstStatement astStatement){
        this.astStatement = astStatement;
        arguments = new ArrayList<>();
    }
    @Getter
    private int nIndex = 0;
    protected boolean separate = false;
    protected void incrementNIndex(){
        nIndex++;
    }
    @Setter @Getter
    private boolean mustBeWritten = false;
    protected List<String> arguments;
    @Getter @Setter
    private String returning;
    protected abstract void validArguments() throws InvalidYesSyntaxException;
    protected abstract void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException;
    protected abstract List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException;
    public AstStatement astStatement;
    public void generate(FileOutputStream out, boolean writeOut, boolean newScope) throws IOException, InvalidYesSyntaxException {
        validArguments();
        if((hasNs()&&newScope)||hasPop()) {
            out.write("{".getBytes());
            setNestedIndexes();
        }
        applyPrefixes(out);
        arguments=writeArguments(out);
        if(arguments!=null) {
            for (String argument : arguments) {
                if (argument == null)
                    throw new InvalidYesSyntaxException(astStatement.getLine(), "Null returning single nested statement!");
            }
        }
        setReturning();
        if(writeOut || mustBeWritten){
            write(out);
        }
        if((hasNs()&&newScope)||hasPop())out.write("}".getBytes());
    }

    private void setNestedIndexes() {
        astStatement.getArguments().stream().filter(a->a instanceof NestedStatement).forEach((s)-> {
            ((NestedStatement) s).setNIndex(getNIndex());
            incrementNIndex();
        });
    }

    private boolean hasNs(){
        return astStatement.getArguments().stream().filter(a-> a instanceof NestedStatement).anyMatch(a->!((NestedStatement) a).isSingleStatement());
    }
    private boolean hasPop(){
        return astStatement.getArguments().stream()
                .filter(a-> a instanceof NestedStatement)
                .filter(a-> ((NestedStatement) a).isSingleStatement())
                .anyMatch(a->((NestedStatement) a).getStack().get(0).getToken().equals(StatementTokens.POP.getToken()));
    }
    protected void setReturning() throws InvalidYesSyntaxException {}
    protected  void applyPrefixes(FileOutputStream out)throws IOException{
    }
}
