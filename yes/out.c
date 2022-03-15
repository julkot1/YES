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
unsigned char stdGETC();
char *stdGETS(unsigned long ar0);
int stdtoINT(char *ar0);
int stdABS(int ar0);
void stdECHO(char *ar0)
{
    size_t pta = 1;
    *((unsigned long *)rx) = strlen(((char *)ar0));
    *((int *)rx) = syscall(1, 1, ((char *)ar0), (*((unsigned long *)rx)));
}
unsigned char stdGETC()
{
    size_t pta = 0;
    gr[ptg] = malloc(sizeof(char) * 1);
    ptg++;
    *((int *)rx) = syscall(0, 1, ((char *)gr[ptg - 1 - 0]), 1);
    return (((char *)gr[ptg - 1 - 0])[0] - '0');
}
char *stdGETS(unsigned long ar0)
{
    size_t pta = 1;
    gr[ptg] = malloc(sizeof(char) * ar0);
    ptg++;
    *((int *)rx) = syscall(0, 1, ((char *)gr[ptg - 1 - 0]), (1 + ar0));
    return ((char *)gr[ptg - 1 - 0]);
}
int stdtoINT(char *ar0)
{
    size_t pta = 1;
    gr[ptg] = malloc(sizeof(int));
    *((int *)gr[ptg]) = strlen(((char *)ar0));
    ptg++;
    gr[ptg] = malloc(sizeof(int));
    *((int *)gr[ptg]) = 0;
    ptg++;
    {
        for (size_t i = 0; i < *((int *)gr[ptg - 1 - 1]); i++)
        {
            *(int *)rx = *((int *)pop(sizeof(int)));
            gr[ptg] = malloc(sizeof(int));
            *((int *)gr[ptg]) = (((*((int *)rx)) * 10) + (((char *)ar0)[i] - '0'));
            ptg++;
        }
    }
    {
        return *((int *)pop(sizeof(int)));
    }
}
int stdABS(int ar0)
{
    size_t pta = 1;
    return ((ar0) > (0) ? ar0 : (-1 * ar0));
}
int main()
{
    gr = malloc(PTG_SIZE * sizeof(void *));
    rx = malloc(sizeof(long long));
    *((int *)rx) = stdtoINT("123");
    {
        char *buffer;
        asprintf(&buffer, "%d\n", (*((int *)rx)));
        rx = strdup(buffer);
    }
    stdECHO(strdup((char *)rx));
    for (size_t i = 0; i < PTG_SIZE; i++)
    {
        gr[i] = malloc(0);
        free(*(gr + i));
    }
    free(gr);
    return 0;
}
