#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/syscall.h>
#define PTG_SIZE 300000
void **gr;
unsigned long ptg = 0;
void *rx;
void *pop(size_t size)
{
    void *el = malloc(size);
    ptg--;
    memcpy(el, gr[ptg], size);
    return el;
}
void stdECHO(char *ar0);
void stdECHO(char *ar0)
{
    size_t pta = 1;
    *((unsigned long *)rx) = strlen(((char *)ar0));
    *((int *)rx) = syscall(1, 1, ((char *)ar0), *((unsigned long *)rx));
}
int main()
{
    gr = malloc(PTG_SIZE * sizeof(void *));
    rx = malloc(sizeof(long long));
    gr[ptg] = malloc(sizeof(int));
    *((int *)gr[ptg]) = 500000000;
    ptg++;
    gr[ptg] = malloc(sizeof(int));
    *((int *)gr[ptg]) = 1;
    ptg++;
    {
        for (size_t i = 0; i < *((int *)gr[ptg - 1 - 1]); i++)
        {
            *((int *)rx) = (*((int *)gr[ptg - 1 - 1]) % *((int *)gr[ptg - 1 - 0]));
            *((unsigned char *)rx) = (*((int *)rx)) == (0);
            {
                if (*((unsigned char *)rx))
                {
                    {
                        char *buffer;
                        asprintf(&buffer, "%d\n", *((int *)gr[ptg - 1 - 0]));
                        rx = strdup(buffer);
                    }
                    stdECHO(strdup((char *)rx));
                }
            }
            *((int *)gr[ptg - 1]) = (*((int *)gr[ptg - 1 - 0]) + 1);
        }
    }
    for (size_t i = 0; i < PTG_SIZE; i++)
    {
        gr[i] = malloc(0);
        free(*(gr + i));
    }
    free(gr);
    return 0;
}
