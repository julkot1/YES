package pl.julkot1.yes.generator;

import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.generator.parser.StatementParser;

import java.io.FileOutputStream;
import java.io.IOException;

import static pl.julkot1.yes.generator.GeneratorConstance.*;
public class CGenerator {
    private static void writeOpening(FileOutputStream out)throws IOException{
        out.write(IMPORTS.getBytes());
        out.write(DEFINES.getBytes());
    }
    public static void generate(AST ast, String fileName) throws IOException {
        FileOutputStream out = new FileOutputStream(fileName);
        writeOpening(out);
        writeAST(out, ast);
        out.close();
    }

    private static void writeAST(FileOutputStream out, AST ast) throws IOException{
        out.write(MAIN_OPEN.getBytes());
        out.write(GR_DEFINE.getBytes());
        out.write(XR_DEFINE.getBytes());

        for (AstStatement astStatement : ast.getStatementList())
            StatementParser.writeStatement(astStatement, out);
        out.write(FREE_GR.getBytes());
        out.write(FREE_XR.getBytes());
        out.write(FILE_END.getBytes());
    }
}
