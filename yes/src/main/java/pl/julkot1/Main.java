package pl.julkot1;

import pl.julkot1.yes.exception.InvalidYesSyntaxException;
import pl.julkot1.yes.generator.CGenerator;
import pl.julkot1.yes.project.File;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {
    public static Set<File> dependencies = new HashSet<>();
    public static File file;
    public static String STD_PATH = "compiler/std";
    public static int POP_INDEX= 0;
    public static void main(String[] args) {
        try{
            file = new File(args[0], true);
            if(args.length==2){
                STD_PATH = args[1];
            }
            CGenerator.generate(file, "out.c");
        }catch (IOException | InvalidYesSyntaxException err){
            err.printStackTrace();
        }
    }
}
