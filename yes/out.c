#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/syscall.h>
#define PTG_SIZE 300000
void **gr;
void **xr;
int main()
{
    gr = malloc(PTG_SIZE * sizeof(void *));
    unsigned long ptg = 0;
    xr = malloc(16 * sizeof(void *));
    for (size_t i = 0; i < 16; i++)
        xr[i] = malloc(32 * sizeof(char));
    {
        int buffer;
        scanf("%i", &buffer);
        *((int *)xr[2]) = buffer;
    }
    gr[ptg] = malloc(sizeof(int));
    *((int *)gr[ptg]) = 1;
    ptg++;
    gr[ptg] = malloc(sizeof(int));
    *((int *)gr[ptg]) = 1;
    ptg++;
    {
        for (size_t i = 0; i < *((int *)xr[2]); i++)
        {
            *((size_t *)xr[1]) = i;
            *((int *)xr[0]) = (*((int *)gr[ptg - 1 - 0]) + *((int *)gr[ptg - 1 - 1]));
            *((int *)xr[3]) = *((int *)gr[ptg - 1 - 0]);
            for (int i = 0; i < 2; i++)
            {
                free(gr[ptg - i]);
            }
            ptg -= 2;
            gr[ptg] = malloc(sizeof(int));
            *((int *)gr[ptg]) = *((int *)xr[3]);
            ptg++;
            gr[ptg] = malloc(sizeof(int));
            *((int *)gr[ptg]) = *((int *)xr[0]);
            ptg++;
        }
    }
    {
        char *buffer;
        asprintf(&buffer, "%i\n", *((int *)gr[ptg - 1 - 0]));
        xr[0] = strdup(buffer);
    }
    *((int *)xr[0]) = syscall(1, 1, ((char *)xr[0]), strlen(((char *)xr[0])));
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
