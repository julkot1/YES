package pl.julkot1.yes.generator.parser;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.UndefinedStatement;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.statement.StatementTokens;

import java.io.FileOutputStream;
import java.io.IOException;

public class StatementParser {
    public static void writeStatement(AstStatement astStatement, FileOutputStream out) throws IOException{
        var st = StatementTokens.getByToken(astStatement.getToken());
        //TODO include custom _STATEMENT
        if(st.isEmpty())throw new UndefinedStatement(astStatement.getLine(), astStatement.getToken());
        try{
        Statement a = (Statement) st.get().getClazz().getConstructors()[0].newInstance(astStatement);
            a.generate(out);
        }catch (Exception ignored){}

    }
}
