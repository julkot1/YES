package pl.julkot1.yes.generator;

import pl.julkot1.Main;
import pl.julkot1.yes.ast.AST;
import pl.julkot1.yes.ast.models.AstStatement;
import pl.julkot1.yes.exception.ErrorCodes;
import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.parser.StatementParser;
import pl.julkot1.yes.project.File;
import pl.julkot1.yes.statement.StatementTokens;

import java.io.FileOutputStream;
import java.io.IOException;

import static pl.julkot1.yes.generator.GeneratorConstance.*;

public class CGenerator {
    private static void writeOpening(FileOutputStream out) throws IOException {
        out.write(IMPORTS.getBytes());
        out.write(DEFINES.getBytes());
    }

    public static void generate(File main, String fileName) throws IOException, InvalidYesSyntaxException {
        FileOutputStream out = new FileOutputStream(fileName);
        writeOpening(out);
        getModules(main);
        var ast = main.getAst();
        for (AstStatement astStatement : ast.getStatementList()) {
            if (astStatement.getToken().equals(StatementTokens.INTERFACE_DEF.getToken()))
                StatementParser.writeStatement(astStatement, out, true);
        }
        writeDependencies(out);
        for (AstStatement astStatement : ast.getStatementList()) {
            if (astStatement.getToken().equals(StatementTokens.STATEMENT_DEF.getToken()))
                StatementParser.writeStatement(astStatement, out, true);
        }
        Main.file = main;
        writeMain(out, ast);
        out.close();
    }

    private static void writeDependencies(FileOutputStream out) throws IOException, InvalidYesSyntaxException {
        for (File dependency : Main.dependencies) {
            Main.file = dependency;
            var ast = dependency.getAst();
            for (AstStatement astStatement : ast.getStatementList()) {
                if (astStatement.getToken().equals(StatementTokens.INTERFACE_DEF.getToken()))
                    StatementParser.writeStatement(astStatement, out, true);
                else if (!astStatement.getToken().equals(StatementTokens.STATEMENT_DEF.getToken()))
                    throw new InvalidYesSyntaxException(astStatement, ErrorCodes.MODULE_FORBIDDEN_STATEMENT_INVOKE);
            }
        }
        for (File dependency : Main.dependencies) {
            Main.file = dependency;
            var ast = dependency.getAst();
            for (AstStatement astStatement : ast.getStatementList()) {
                if (astStatement.getToken().equals(StatementTokens.STATEMENT_DEF.getToken()))
                    StatementParser.writeStatement(astStatement, out, true);
            }
        }
    }

    private static void getModules(File file) throws InvalidYesSyntaxException, IOException {
        Main.file = file;
        if (!(file.isMain() || file.isModule()))
            throw new InvalidYesSyntaxException(0,
                    "is not module\n code error: " + ErrorCodes.HEADER_NOT_MODULE_IMPORT);
        var dep = file.getDependenciesPaths();
        for (String s : dep) {
            Main.dependencies.add(new File(s, false));
        }
        for (String s : dep) {
            getModules(new File(s, false));
        }
    }

    private static void writeMain(FileOutputStream out, AST ast) throws IOException {

        out.write(MAIN_OPEN.getBytes());
        out.write(GR_DEFINE.getBytes());
        out.write(XR_DEFINE.getBytes());

        for (AstStatement astStatement : ast.getStatementList()) {
            if (!astStatement.getToken().equals(StatementTokens.STATEMENT_DEF.getToken()) &&
                    !astStatement.getToken().equals(StatementTokens.INTERFACE_DEF.getToken()))
                StatementParser.writeStatement(astStatement, out, true);
        }
        out.write(FREE_GR.getBytes());
        out.write(FREE_XR.getBytes());
        out.write(FILE_END.getBytes());
    }
}
