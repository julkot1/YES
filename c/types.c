#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main()
{
    void **cr = malloc(5 * sizeof(void *));
    int ptc = 0;
    void **pr = cr;
    *(cr + ptc) = malloc(sizeof(int));
    *((int *)cr[ptc]) = 4;
    ptc++;
    printf("%i\n", *((int *)pr[ptc - 1]));
    *((int *)pr[ptc - 1]) = -5;
    printf("%i\n", *((int *)cr[ptc - 1]));
    return 0;
}