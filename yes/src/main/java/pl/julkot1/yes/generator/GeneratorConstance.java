package pl.julkot1.yes.generator;

public class GeneratorConstance {
    public static final String IMPORTS = "#define _GNU_SOURCE\n#include <stdio.h>\n#include <stdlib.h>\n#include <unistd.h>\n#include <string.h>\n#include <sys/syscall.h>\n";
    public static final String DEFINES = "#define PTG_SIZE 300000\nvoid **gr;void **xr;";
    public static final String MAIN_OPEN = "int main(){";
    public static final String GR_DEFINE = "gr = malloc(PTG_SIZE * sizeof(void *));unsigned long ptg = 0;";
    public static final String XR_DEFINE = "xr = malloc(16 * sizeof(void *));for (size_t i = 0; i < 16; i++)xr[i] = malloc(32 * sizeof(char));";
    public static final String FREE_GR = "for (size_t i = 0; i < PTG_SIZE; i++){gr[i] = malloc(0);free(*(gr+i));}free(gr);";
    public static final String FREE_XR = "for (size_t i = 0; i < 16; i++){xr[i] = malloc(0);free(*(xr+i));}free(xr);";
    public static final String FILE_END = "return 0;}\n";
    public static final String STATEMENT_NAME_PATTERN = "[?a-zA-Z?_?0-9]+";

}
