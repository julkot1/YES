package pl.julkot1.yes.statement.custom.interfaces;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.julkot1.yes.types.Type;

@AllArgsConstructor
@Getter
public class ArgumentCount {
    private int value;
    private Type type;
}
