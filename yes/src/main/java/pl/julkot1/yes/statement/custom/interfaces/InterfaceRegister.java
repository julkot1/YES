package pl.julkot1.yes.statement.custom.interfaces;

import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.statement.StatementTokens;
import pl.julkot1.yes.statement.custom.CustomStatement;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class InterfaceRegister {
    static  {
        interfaces = new HashSet<>();
    }
    private static final Set<Interface> interfaces;
    public static boolean contains(String token){
        return interfaces.stream().anyMatch(s->s.getToken().equals(token));
    }
    public static void add(Interface anInterface) throws InvalidYesSyntaxException {
        if(StatementTokens.getByToken( anInterface.getToken()).isPresent())
            throw new InvalidYesSyntaxException(anInterface.astStatement.getLine(), anInterface.getToken()+" has already been defined!");
        if(!interfaces.add(anInterface))
            throw new InvalidYesSyntaxException(anInterface.astStatement.getLine(), anInterface.getToken()+" has already been defined!");
    }
    public static Optional<Interface> get(String token) throws InvalidYesSyntaxException {
        return interfaces.stream().filter(s->s.getToken().equals(token)).findAny();
    }
}
