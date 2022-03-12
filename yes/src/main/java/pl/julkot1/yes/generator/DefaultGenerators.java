package pl.julkot1.yes.generator;

import org.apache.commons.lang3.math.NumberUtils;
import pl.julkot1.yes.ast.models.*;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.NestedStatementException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.generator.parser.ArrayParser;
import pl.julkot1.yes.generator.parser.StatementParser;
import pl.julkot1.yes.generator.parser.ValueParser;
import pl.julkot1.yes.lexer.tokens.PrefixTokens;
import pl.julkot1.yes.lexer.tokens.SpecialTypeTokens;
import pl.julkot1.yes.statement.StatementRegister;
import pl.julkot1.yes.statement.StatementTokens;
import pl.julkot1.yes.statement.custom.interfaces.InterfaceRegister;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.util.ArgumentsArray;
import pl.julkot1.yes.util.StatementUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefaultGenerators {
    public static List<String> writeArguments(List<Argument> arguments, FileOutputStream out)
            throws IOException, InvalidYesSyntaxException {
        List<String> args = new ArrayList<>();
        for (Argument argument : arguments) {
            if (argument.getClass().toString().equals(Value.class.toString()))
                args.add(putValueToCr((Value) argument));
            else if (argument.getClass().toString().equals(Array.class.toString()))
                args.add(putArrayToCr((Array) argument, out));
            else if (argument.getClass().toString().equals(NestedStatement.class.toString())) {
                args.add(writeNestedStatement((NestedStatement) argument, out));
            }
        }
        return args;
    }

    private static String writeNestedStatement(NestedStatement argument, FileOutputStream out) throws IOException {
        if (argument.isSingleStatement()) {
            var st = (AstStatement) argument.getStack().get(0);
            var ret = StatementParser.writeStatement(st, out, false);
            if (argument.getType() == null || argument.getType() == Type.NULL)
                argument.setType(st.getType());
            return ret;
        } else {
            for (Argument astStatement : argument.getStack()) {
                if (!(astStatement instanceof AstStatement))
                    throw new NestedStatementException(astStatement.getLine(), "(incorrect usage of statement)");
                StatementParser.writeStatement((AstStatement) astStatement, out, true);
            }

        }
        return "n" + argument.getNIndex();

    }

    public static String putArrayToCr(Array array, FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        if (array.getIndex().getArgument() instanceof Value) {
            if (!ValueParser.castToSize((Value) array.getIndex().getArgument()))
                throw new TypeException(array.getLine(), array.getToken(), "array index must be Size type");
            if (array.getType() == Type.NULL && !array.getToken().equals(SpecialTypeTokens.AR.getToken()))
                throw new TypeException(array.getLine(), array.getToken(),
                        "to take an element from the array it's type must be given");
        }
        if (array.getIndex().getArgument() instanceof AstStatement) {
            StatementParser.writeStatement((AstStatement) array.getIndex().getArgument(), out, true);
        }
        checkAR(array);
        if (array.getType() == null)
            throw new TypeException(array.getLine(),
                    array.getToken() + "[" + array.getIndex().getArgument().getToken() + "]", "missing type");
        return ArrayParser.getElement(array);

    }

    private static void checkAR(Array array) throws InvalidYesSyntaxException {
        if (array.getToken().equals(SpecialTypeTokens.AR.getToken())) {
            var definition = StatementUtils.getParent(array, StatementTokens.STATEMENT_DEF.getToken());
            if (definition.isPresent()) {
                var statement = StatementRegister.get(definition.get().getArgument(0));
                var anInterface = InterfaceRegister.get(statement.getToken(), statement.getNamespace());
                if (anInterface.isPresent()) {
                    var index = array.getIndex().getArgument().getToken();
                    var type = StatementUtils.argumentType(anInterface.get().getArgumentCounts(),
                            NumberUtils.toInt(index));
                    if (type.equals(Type.NULL))
                        throw new TypeException(array.getLine(), array.getToken(),
                                "unknown argument (" + index + " > pta)");
                    array.setType(type);
                } else
                    throw new InvalidYesSyntaxException(array.getLine(),
                            "_STATEMENT " + statement.getToken() + " doesn't have interface (has no arguments)");
            } else
                throw new InvalidYesSyntaxException(array.getLine(), "ar is only available in _STATEMENT definition");

        }
    }

    public static String getToken(Value value) {
        if (value.getToken().equals("true"))
            return "1";
        if (value.getToken().equals("false"))
            return "0";
        if (value.getType() == Type.STR)
            return Type.StrToCString(value.getToken());
        if (value.getToken().equals(SpecialTypeTokens.PTA.getToken()))
            return "*pta";
        return value.getToken();
    }

    public static String putValueToCr(Value value) {
        if (value.getToken().equals(SpecialTypeTokens.RESULT.getToken())) {
            if (value.getType().equals(Type.STR))
                return "((char *)rx)";
            return "(*((" + value.getType().getCToken() + "*)rx))";

        }
        value.setType(ValueParser.getValueType(value));
        return getToken(value);
    }
}
