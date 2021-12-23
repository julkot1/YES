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
        void **cr = malloc(1 * sizeof(void *));
        int ptc = 0;
        *(cr + ptc) = malloc(sizeof(int));
        *((int *)cr + ptc) = 2;
        ptc++;
        gr[ptg] = malloc(sizeof(int));
        *((int *)gr[ptg]) = *((int *)cr);
        ptg++;
        free(cr);
    };
    {
        void **cr = malloc(2 * sizeof(void *));
        int ptc = 0;
        *(cr + ptc) = malloc(sizeof(int));
        *((int *)cr + ptc) = 1;
        ptc++;
        *(cr + ptc) = malloc(sizeof(int));
        *((int *)cr + ptc) = 2;
        ptc++;
        xr[ptx] = malloc(sizeof(int));
        *((int *)xr[ptx]) = *((int *)cr) + *((int *)cr + 1);
        ptx++;
        free(cr);
    };
    {
        void **cr = malloc(1 * sizeof(void *));
        int ptc = 0;
        *(cr + ptc) = malloc(sizeof(int));
        *((int *)cr + ptc) = *(xr + ptx - 1);
        ptc++;
        printf("%d\n", *((int *)(*cr)));
        free(cr);
    };
    for (int i = 0; i < PTX_SIZE; i++)
    {
        free(*(xr + i));
    }
    free(xr);
    for (int i = 0; i < PTG_SIZE; i++)
    {
        free(*(gr + i));
    }
    free(gr);
    return 0;
}