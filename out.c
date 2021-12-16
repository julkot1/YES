#include <stdio.h>
#include <stdlib.h>
#define PTG_SIZE 300000
#define PTX_SIZE 50000
int main()
{
    int *gr = (int *)malloc(PTG_SIZE * sizeof(int));
    int *xr = (int *)malloc(PTX_SIZE * sizeof(int));
    int ptr = 0, ptx = 0;
    free(xr);
    free(gr);
    return 0;
}