#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main()
{
    void **gr = malloc(5 * sizeof(void *));
    int ptg = 0;

    gr[ptg] = malloc(sizeof(int));
    *((int *)gr[ptg]) = 5;

    void **cr = malloc(5 * sizeof(void *));
    int ptc = 0;

    *(cr + ptc) = gr[ptg];
    ptc++;
    *((int *)gr[ptg]) = 3;

    printf("%d", *((int *)cr[ptc - 1]));
    return 0;
}