package pl.julkot1.yes.generator.parser;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.UndefinedStatement;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.StatementRegister;
import pl.julkot1.yes.statement.StatementTokens;
import pl.julkot1.yes.statement.custom.CustomStatement;
import pl.julkot1.yes.statement.custom.CustomStatementImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class StatementParser {
    public static String writeStatement(AstStatement astStatement, FileOutputStream out, boolean writeOut) throws IOException{
        var st = StatementTokens.getByToken(astStatement.getToken());
        //if(st.isEmpty() && !StatementRegister.contains(astStatement.getToken()))throw new UndefinedStatement(astStatement.getLine(), astStatement.getToken());
        try{
            Statement a;
            if(st.isEmpty()){
                a = new CustomStatementImpl(astStatement);
            }else {
                a = (Statement) st.get().getClazz().getConstructors()[0].newInstance(astStatement);
            }
            a.generate(out, writeOut);
            return a.getReturning();
        }catch (InvalidYesSyntaxException | InstantiationException | IllegalAccessException | InvocationTargetException exception){exception.printStackTrace();}
        return null;
    }
}
