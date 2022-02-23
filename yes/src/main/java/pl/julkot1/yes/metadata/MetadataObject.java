package pl.julkot1.yes.metadata;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MetadataObject {
    private final String keyword;
    private final Type type;
    @Setter
    private String[] args;

    public MetadataObject(Type type, String keyword, int argsSize) {
        this.type = type;
        this.args = new String[argsSize];
        this.keyword = keyword;

    }

    public enum Type {
        MAIN,
        MANDATORY,
        OPTIONAL;
    }
}
