package pl.julkot1.yes.project;

import lombok.Getter;
import pl.julkot1.Main;
import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.exception.ErrorCodes;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.Lexer;
import pl.julkot1.yes.metadata.MetadataBuilder;
import pl.julkot1.yes.metadata.MetadataObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Getter
public class File {
    private final AST ast;
    private final List<MetadataObject[]> metadata;
    private final String path;
    private String namespace = "_GLOBAL";
    private boolean module=false;
    private final boolean main;
    public File(String path, boolean main) throws InvalidYesSyntaxException, IOException {
        this.path = path;
        this.main = main;
        Main.file = this;
        var rawMetadata = new ArrayList<String[]>();
        var tokens = Lexer.simplify(Lexer.resolveFile(path,rawMetadata));
        ast = AST.build(tokens);
        metadata = MetadataBuilder.build(rawMetadata);
        setModule();
        setNamespace();
    }
    public File(String text, ArrayList<String[]> rawMetadata) throws InvalidYesSyntaxException, IOException {
        this.path = null;
        this.main = true;
        Main.file = this;
        var tokens = Lexer.simplify(Lexer.resolve(text));
        ast = AST.build(tokens);
        metadata = MetadataBuilder.build(rawMetadata);
        setModule();
        setNamespace();
    }
    private void setNamespace() throws InvalidYesSyntaxException {
        var list = metadata.stream().filter(e -> e[0].getKeyword().equals("namespace")).map(e->e[0].getArgs()[0]).toList();
        if(list.size()>1)throw new InvalidYesSyntaxException(0, "multiple namespace declaration");
        if(list.size()==1)namespace = list.get(0);
        else namespace ="_GLOBAL";
    }

    private void setModule(){
        module = metadata.stream().anyMatch(e -> e[0].getKeyword().equals("module"));
    }
    public HashSet<String> getDependenciesPaths() throws InvalidYesSyntaxException {
        var list = metadata.stream().filter(e -> e[0].getKeyword().equals("import"))
                .map(a -> a[0].getArgs()[0]).toList();
        var importsSet = new HashSet<String>();
        for (String s : list) {
            if(!importsSet.add(s))
                throw new InvalidYesSyntaxException(0, "multiple imports "+s+"\n error code: "+ ErrorCodes.HEADER_MULTIPLE_IMPORTS);
        }
        return importsSet;
    }
}
