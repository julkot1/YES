package pl.julkot1.yes.util;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.custom.CustomStatement;
import pl.julkot1.yes.statement.custom.interfaces.ArgumentCount;
import pl.julkot1.yes.statement.custom.interfaces.Interface;
import pl.julkot1.yes.statement.custom.interfaces.InterfaceRegister;
import pl.julkot1.yes.types.Type;

import java.io.FileOutputStream;
import java.io.IOException;

public class DeclarationUtils {

    public static void createFunctionDefinition(CustomStatement customStatement, FileOutputStream out) throws InvalidYesSyntaxException, IOException {
        if(InterfaceRegister.contains(customStatement.getToken())){
            var anInterface = InterfaceRegister.get(customStatement.getToken(), customStatement.getNamespace()).get();
            var type = anInterface.astStatement.getType();
            var cType = type.equals(Type.STR)?"char*":type.getCToken();
            out.write(String.format("%s %s%s(%s){",
                            type.equals(Type.NULL)?"void":cType,
                            anInterface.getNamespace(),
                            anInterface.astStatement.getArgument(0).getToken(),
                            setArgs(anInterface))
                    .getBytes());
        }
    }

    public static void createFunctionDefinition(Interface anInterface, FileOutputStream out)throws InvalidYesSyntaxException, IOException{
        var type = anInterface.astStatement.getType();
        var cType = type.equals(Type.STR)?"char*":type.getCToken();
        out.write(String.format("%s %s%s(%s);",
                        type.equals(Type.NULL)?"void":cType,
                        anInterface.getNamespace(),
                        anInterface.astStatement.getArgument(0).getToken(),
                        setArgs(anInterface))
                .getBytes());
    }


    public static String setArgs(Interface anInterface) throws InvalidYesSyntaxException {
        StringBuilder argv = new StringBuilder();int count = 0;
        for (ArgumentCount argumentCount : anInterface.getArgumentCounts()) {
            for (int i = 0; i < argumentCount.getValue(); i++) {
                var type = argumentCount.getType();
                argv.append(type.equals(Type.STR)?"char *":type.getCToken()).append(" ar").append(count).append(",");
                count++;
            }
        }
        return argv.substring(0, argv.toString().length()-1);
    }
}
