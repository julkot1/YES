package pl.julkot1.yes.ast.builder;

import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.ast.models.Value;
import pl.julkot1.yes.ast.scope.Scope;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.parser.ValueParser;
import pl.julkot1.yes.lexer.tokens.SyntaxTokens;
import pl.julkot1.yes.lexer.tokens.Token;
import pl.julkot1.yes.lexer.tokens.TokenType;
import pl.julkot1.yes.types.Type;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class StatementBuilder extends Builder<AstStatement> {
    @Override
    protected void build() throws InvalidYesSyntaxException {
        if(type!=null)inst.setType(type);
        type = null;
        addPrefixes(inst);
       Argument argument;
       do{

           argument = getStatementArgument();
           if(argument!=null) {
               inst.addArgument(argument);
           }
       }while (argument!=null);
        scope.shift(1);
        scope.updateTokens();
    }
    private Argument getStatementArgument() throws InvalidYesSyntaxException {
        AtomicReference<Argument> argument = new AtomicReference<>();
        scope.iterate((t, prev, next, index) -> {
            boolean isNext = true;
            switch (t.type()){
                case PREFIX -> this.getPrefix(t);
                case TYPE -> this.setType(t);
                case VALUE, SPECIAL-> {
                    argument.set(buildValue(t));
                    isNext = false;
                }
                case SYNTAX -> {
                    if(t.obj().equals(SyntaxTokens.END_LINE)) {
                        scope.shift(-1);
                        isNext = false;
                    }
                    else if(t.obj().equals(SyntaxTokens.NESTED_OPEN)){
                        var nestedBuilder = new NestedBuilder().parse(type, prefixes, scope, index, inst);
                        argument.set(nestedBuilder.inst);
                        isNext = false;
                        type = null;
                        prefixes = new ArrayList<>();
                    }
                }
                case ARRAY -> {
                    var arrayBuilder = new ArrayBuilder().parse(type, prefixes, scope, index, inst);
                    argument.set(arrayBuilder.inst);
                    isNext = false;
                    type = null;
                    prefixes = new ArrayList<>();
                }

            }
            scope.shift(1);
            return isNext;
        });
        scope.updateTokens();
        return argument.get();

    }
    private Value buildValue(Token t){
        var typeY = Type.NULL;
        if(type!=null)typeY = type;
        else typeY = ValueParser.getValueType(new Value(typeY, t.toString(), t.line(), inst));
        var value = new Value(typeY, t.toString(), t.line(), inst);
        addPrefixes(value);
        type = null;
        prefixes = new ArrayList<>();
        return  value;
    }
    @Override
    protected void createScope(Scope rawScope, int shift) throws InvalidYesSyntaxException {
        this.scope = rawScope;
        this.scope.shift(shift);
        this.scope.updateTokens();

        var first = this.scope.getTokens().get(0);
        if(!first.type().equals(TokenType.STATEMENT)) throw new InvalidYesSyntaxException(first.line(), "Nobody expect "+first+" token! (Statement should be better choice)");

        inst = new AstStatement(first.toString(), first.line(), this.scope.getParent());

        this.scope.shift(1);
        this.scope.updateTokens();
    }

}
