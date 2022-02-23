package pl.julkot1.yes.metadata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.julkot1.yes.lexer.tokens.SyntaxTokens;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Optional;


@Getter
@AllArgsConstructor
public enum DefaultMetadata {
    IMPORT(new MetadataObject[]{
            new MetadataObject(MetadataObject.Type.MAIN, "import", 1),
            new MetadataObject(MetadataObject.Type.OPTIONAL, "as", 1),
    }),
    MODULE(new MetadataObject[]{
            new MetadataObject(MetadataObject.Type.MAIN, "module", 0)
    }),
    NAMESPACE(new MetadataObject[]{
        new MetadataObject(MetadataObject.Type.MAIN, "namespace", 1)
    });
    final MetadataObject[] objects;
    static Optional<DefaultMetadata> getByToken(String token){
        var list = new ArrayList<>(EnumSet.allOf(DefaultMetadata.class));
        return list.stream().filter(m->m.objects[0].getKeyword().equals(token)).findFirst();
    }
    MetadataObject implement(int index){
        var template =  this.objects[index];
        return new MetadataObject( template.getType(), template.getKeyword(), template.getArgs().length);
    }

}
