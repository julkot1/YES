#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#define PTG_SIZE 300000
#define PTX_SIZE 50000
int main()
{
    void **gr = malloc(PTG_SIZE * sizeof(void *));
    void **xr = malloc(PTX_SIZE * sizeof(void *));
    int ptg = 0, ptx = 0;
    {
        int *cr = (int *)malloc(1 * sizeof(int));
        int ptc = 0;
        *(cr + ptc) = 2;
        ptc++;
        *(gr + ptg) = malloc(sizeof(Int));
        *((Int *)gr + ptg) = *((Int *)cr);
        ptg++;
        free(cr);
    };
    for (int i = 0; i < PTX_SIZE; i++)
    {
        free(*(xr + i))
    }
    free(xr);
    for (int i = 0; i < PTG_SIZE; i++)
    {
        free(*(gr + i))
    }
    free(gr);
    return 0;
}