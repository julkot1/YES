package pl.julkot1.yes.statement;

import pl.julkot1.Main;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.ErrorCodes;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.custom.CustomStatement;

import java.util.HashSet;
import java.util.Set;

public class StatementRegister {
    static  {
        statements = new HashSet<>();
    }
    private static final Set<CustomStatement> statements;
    public static boolean contains(String token){
        return statements.stream().anyMatch(s->s.getToken().equals(token));
    }
    public static void add(CustomStatement statement) throws InvalidYesSyntaxException {
        if(StatementTokens.getByToken(statement.getToken()).isPresent())
            throw new InvalidYesSyntaxException(statement.astStatement.getLine(), statement.getToken()+" has already been defined!");
        if(!statements.add(statement))
            throw new InvalidYesSyntaxException(statement.astStatement.getLine(), statement.getToken()+" has already been defined!");
    }
    public static CustomStatement get(Argument arg) throws InvalidYesSyntaxException {
        if(arg.getNamespace()==null)arg.setNamespace("_GLOBAL");
        var element = statements.stream().filter(s->
                s.getToken().equals(arg.getToken()) &&
                        (Main.file.getNamespace().equals(s.getNamespace())&&arg.getNamespace().equals("_GLOBAL")) || s.getNamespace().equals(arg.getNamespace())
        ).findAny();
        if(element.isEmpty())
            throw new InvalidYesSyntaxException(arg, ErrorCodes.UNKNOWN_STATEMENT);
       return element.get();
    }
}
