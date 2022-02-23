package pl.julkot1.yes.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.lexer.Lexer;
import pl.julkot1.yes.metadata.MetadataBuilder;
import pl.julkot1.yes.metadata.MetadataObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class File {
    private final AST ast;
    private final List<MetadataObject[]> metadata;
    private final String path;

    public File(String path) throws InvalidYesSyntaxException, IOException {
        this.path = path;
        var rawMetadata = new ArrayList<String[]>();
        var tokens = Lexer.simplify(Lexer.resolveFile(path,rawMetadata));
        ast = AST.build(tokens);
        metadata = MetadataBuilder.build(rawMetadata);
    }
}
