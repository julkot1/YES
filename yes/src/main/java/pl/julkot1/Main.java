package pl.julkot1;

import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.CGenerator;
import pl.julkot1.yes.project.File;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static Set<String> dependencies = new HashSet<>();
    public static void main(String[] args) {
        try{
            var main = new File(args[0]);
            CGenerator.generate(main.getAst(), "out.c");
        }catch (IOException | InvalidYesSyntaxException err){
            err.printStackTrace();
        }
    }
}
