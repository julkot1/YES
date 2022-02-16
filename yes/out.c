#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/syscall.h>
#define PTG_SIZE 300000
int main()
{
    void **gr = malloc(PTG_SIZE * sizeof(void *));
    unsigned long ptg = 0;
    void **xr = malloc(16 * sizeof(void *));
    for (size_t i = 0; i < 16; i++)
        xr[i] = malloc(32 * sizeof(char));
    int buffer;
    scanf("%i", &buffer);
    *((int *)xr[2]) = buffer;
    *((int *)xr[0]) = 2 + *((int *)xr[2]);
    for (size_t i = 0; i < PTG_SIZE; i++)
    {
        gr[i] = malloc(0);
        free(*(gr + i));
    }
    free(gr);
    for (size_t i = 0; i < 16; i++)
    {
        xr[i] = malloc(0);
        free(*(xr + i));
    }
    free(xr);
    return 0;
}
