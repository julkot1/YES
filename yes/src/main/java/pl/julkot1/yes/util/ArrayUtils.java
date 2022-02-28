package pl.julkot1.yes.util;

import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.Array;
import pl.julkot1.yes.types.Type;

import java.util.List;

public class ArrayUtils {
    public static void autoTypeset(List<Argument> arguments, Type[] types){
        for (int i = 0; i < types.length; i++) {
            var type= types[i];
            if(type==null) continue;
            var arg = arguments.get(i);
            autoTypeset(arg, type);
        }
    }
    public static void autoTypeset(Argument argument, Type type){
        if(argument instanceof Array && argument.getType().equals(Type.NULL))
            argument.setType(type);
    }
}
