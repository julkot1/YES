#include <stdio.h>
#include <stdlib.h>
#include<unistd.h>
#define PTG_SIZE 300000
#define PTX_SIZE 50000
int main()
{int *gr = (int *)malloc(PTG_SIZE * sizeof(int));int *xr = (int *)malloc(PTX_SIZE * sizeof(int));int ptg = 0, ptx = 0;{int * cr = (int * )malloc(1 * sizeof(int));int ptc=0;*(cr + ptc) = 2; ptc++;*(gr + ptg) = *cr;ptg++;free(cr);};{int * cr = (int * )malloc(1 * sizeof(int));int ptc=0;*(cr + ptc) = 3; ptc++;*(gr + ptg) = *cr;ptg++;free(cr);};{int * cr = (int * )malloc(2 * sizeof(int));int ptc=0;*(cr + ptc) = *(gr + ptg - 1); ptc++;*(cr + ptc) = *(cr + ptc - 1); ptc++;*(xr + ptx) = *cr + *(cr+1);ptx++;free(cr);};{int * cr = (int * )malloc(2 * sizeof(int));int ptc=0;{int * cr = (int * )malloc(2 * sizeof(int));int ptc=0;*(cr + ptc) = 1; ptc++;*(cr + ptc) = *(cr + ptc - 1); ptc++;*(xr + ptx) = *cr + *(cr+1);ptx++;free(cr);};*(cr + ptc) = *(xr + ptx - 1); ptc++;*(cr + ptc) = *(cr + ptc - 1); ptc++;*(xr + ptx) = *cr * *(cr+1);ptx++;free(cr);};{int * cr = (int * )malloc(1 * sizeof(int));int ptc=0;*(cr + ptc) = *(xr + ptx - 1); ptc++;printf("%d\n", *cr);free(cr);};free(xr);free(gr);return 0;}