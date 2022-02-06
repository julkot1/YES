package pl.julkot1.yes.generator;

public class GeneratorConstance {
    public static final String IMPORTS = "#include <stdio.h>\n#include <stdlib.h>\n#include <unistd.h>\n#include <string.h>\n#include <sys/syscall.h>\n";
    public static final String DEFINES = "#define PTG_SIZE 300000\n#define PTX_SIZE 50000\n";
    public static final String MAIN_OPEN = "int main(){";
    public static final String GR_DEFINE = "void **gr = malloc(PTG_SIZE * sizeof(void *));unsigned long ptg = 0;";
    public static final String XR_DEFINE = "void **xr = malloc(PTX_SIZE * sizeof(void *));unsigned long ptx = 0;";
    public static final String FREE_XR = "for (int i = 0; i < PTX_SIZE; i++){xr[i] = malloc(0);free(*(xr+i));}free(xr);";
    public static final String FREE_GR = "for (int i = 0; i < PTG_SIZE; i++){gr[i] = malloc(0);free(*(gr+i));}free(gr);";
    public static final String FILE_END = "return 0;}\n";

}
