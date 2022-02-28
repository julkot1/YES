package pl.julkot1.yes.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.julkot1.yes.ast.models.Argument;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.ErrorCodes;
import pl.julkot1.yes.exception.InvalidArgumentsQuantity;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.exception.TypeException;
import pl.julkot1.yes.types.Type;

import java.util.List;
import java.util.function.Function;

@Getter
@AllArgsConstructor
public class ArgumentsValidation {
    private int quantity;
    private boolean minQuantity;
    private Type[] types;
    private Class<? extends Argument>[] instances;
    private Function<Argument, ErrorCodes>[]  customCheck;
    private boolean[] specifiedTypes;
    public int maxQuantity;

    public static Builder builder(){
        return new Builder();
    }
    public static class Builder {
        private int quantity=0;
        private boolean minQuantity = false;
        private Type[] types;
        private Class<? extends Argument>[] instances;
        private Function<Argument, ErrorCodes>[]  customCheck;
        private boolean[] specifiedTypes;
        public int maxQuantity=0;

        public Builder quantity(int quantity){
            this.quantity = quantity;
            return this;
        }
        public Builder maxQuantity(int maxQuantity){
            this.maxQuantity = maxQuantity;
            return this;
        }
        public Builder minQuantity(){
            this.minQuantity = true;
            return this;
        }
        public Builder enableTypeCheck(){
            types = new Type[quantity+(minQuantity?1:0)];
            specifiedTypes = new boolean[quantity+(minQuantity?1:0)];
            return this;
        }
        public Builder enableInstanceCheck(){
            instances = new Class[quantity+(minQuantity?1:0)];
            return this;
        }
        public Builder enableCustomCheck(){
            customCheck= new Function[quantity+(minQuantity?1:0)];
            return this;
        }
        public Builder customPredicate(int index, Function<Argument, ErrorCodes> predicate){
            customCheck[index] = predicate;
            return this;
        }
        public Builder argumentInstance(int index, Class<? extends Argument> instance){
            instances[index] = instance;
            return this;
        }
        public Builder argumentType(int index, Type instance){
            types[index] = instance;
            return this;
        }
        public Builder argumentRequiredType(int index){
            specifiedTypes[index] = true;
            return this;
        }
        public ArgumentsValidation build(){
            return new ArgumentsValidation(quantity, minQuantity, types, instances, customCheck, specifiedTypes, maxQuantity);
        }
    }
    public void check(List<? extends Argument> arguments, AstStatement parent) throws InvalidYesSyntaxException {
        boolean quantityException = minQuantity ?
                (arguments.size() < quantity || maxQuantity > 0 && (arguments.size() > maxQuantity)):
                arguments.size()!=quantity;
        if(quantityException)
            throw new InvalidArgumentsQuantity(parent.getLine(), parent.getToken(), arguments.size(), quantity, minQuantity);
        if(types!=null)
            checkTypes(arguments);
        if(instances!=null)
            checkInstances(arguments);
        if(customCheck!=null)
            checkCustom(arguments);
    }
    private void checkTypes(List<? extends Argument> arguments) throws InvalidYesSyntaxException {
        for (int i = 0; i < types.length; i++) {
            var type = types[i];
            var arg = arguments.get(i);
            ArrayUtils.autoTypeset(arg,type);
            if(specifiedTypes[i] && arg.getType().equals(Type.NULL))
                throw new InvalidYesSyntaxException(arg, ErrorCodes.TYPE_REQUIRED);
            if(type == null) continue;
            var argType = arg.getType();
            if(type != argType)
                throw new TypeException(arg.getLine(), arg.getToken(), argType.getYesToken(), type.getYesToken());
        }
    }
    private void checkInstances(List<? extends Argument> arguments) throws InvalidYesSyntaxException {
        for (int i = 0; i < instances.length; i++) {
            var instance = instances[i];
            if(instance==null) continue;
            var arg = arguments.get(i);
            if(!arg.getClass().equals(instance))
                throw new InvalidYesSyntaxException(arg.getLine(), arg.getToken()+" must be "+instance.getCanonicalName());
        }
    }
    private void checkCustom(List<? extends Argument> arguments) throws InvalidYesSyntaxException {
        for (int i = 0; i < customCheck.length; i++) {
            var predicate = customCheck[i];
            if(predicate==null) continue;
            var arg = arguments.get(i);
            var code = predicate.apply(arg);
            if(code!= ErrorCodes.SUCCESS){
                throw new InvalidYesSyntaxException(arg, code);
            }
        }
    }
}
