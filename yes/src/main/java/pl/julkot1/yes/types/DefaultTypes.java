package pl.julkot1.yes.types;

import pl.julkot1.yes.ast.models.Argument;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultTypes {
    public static Type getMathType(List<Type> types){
        if(types.stream().anyMatch((s)->s==Type.FLOAT))  return Type.FLOAT;
        if(types.stream().anyMatch((s)->s==Type.LONG))  return Type.LONG;
        if(types.stream().anyMatch((s)->s==Type.INT))  return Type.INT;
        if(types.stream().anyMatch((s)->s==Type.SHORT))  return Type.SHORT;
        if(types.stream().anyMatch((s)->s==Type.CHAR))  return Type.CHAR;
        return Type.NULL;
    }
    public static List<Type> argumentsToTypesList(List<Argument> arguments){
        return arguments.stream()
                .map(Argument::getType)
                .collect(Collectors.toList());
    }
}
