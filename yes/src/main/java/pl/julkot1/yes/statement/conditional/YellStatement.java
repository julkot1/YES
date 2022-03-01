package pl.julkot1.yes.statement.conditional;

import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.DefaultGenerators;
import pl.julkot1.yes.statement.Statement;
import pl.julkot1.yes.types.Type;
import pl.julkot1.yes.util.ArgumentsValidation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class YellStatement extends Statement {
    public YellStatement(AstStatement astStatement) {
        super(astStatement);
    }

    @Override
    protected void validArguments() throws InvalidYesSyntaxException {
        var validator = ArgumentsValidation.builder()
                .quantity(2).enableTypeCheck()
                .argumentType(1, Type.STR)
                .build();
        validator.check(astStatement.getArguments(), astStatement);
        astStatement.getArgument(0).setType(Type.BOOL);
    }

    @Override
    protected void write(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        out.write("if(!*((unsigned char*)cr[0])){".getBytes());
        DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(1)), out);
        var type = astStatement.getArgument(1).getType();
        out.write((String.format("printf(\"%s: by YELL statement: line %d\\n\", *((%s *) cr[1]));", type.getCFormatSpecifier(), astStatement.getLine(),type.getCToken())).getBytes());
        out.write("exit(EXIT_FAILURE);}".getBytes());

    }

    @Override
    protected List<String> writeArguments(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        return DefaultGenerators.writeArguments(List.of(this.astStatement.getArgument(0)), out);
    }
}
