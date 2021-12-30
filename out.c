#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
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
        *(cr + ptc) = malloc(sizeof(char *));
        *((char **)cr[ptc]) = "Type dir name:\x1b[31m ";
        ptc++;
        printf("%s", *((char **)cr[0]));
        free(cr);
    }
    {
        void **cr = malloc(2 * sizeof(void *));
        int ptc = 0;
        *(cr + ptc) = malloc(sizeof(char *));
        *((char **)cr[ptc]) = "mkdir %s";
        ptc++;
        do
        {
            void **cr = malloc(0 * sizeof(void *));
            int ptc = 0;
            char *buffer;
            scanf("%ms", &buffer);
            xr[ptx] = malloc(sizeof(char *));
            *((char **)xr[ptx]) = buffer;
            ptx++;
            free(cr);
        } while (0);
        *(cr + ptc) = malloc(sizeof(char *));
        *((char **)cr[ptc]) = *((char **)xr[ptx - 1]);
        ptc++;
        char buffer[strlen(*((char **)cr[0]))];
        sprintf(buffer, *((char **)cr[0]), *((char **)cr[1]));
        system(buffer);
        free(cr);
    }
    {
        void **cr = malloc(2 * sizeof(void *));
        int ptc = 0;
        *(cr + ptc) = malloc(sizeof(char *));
        *((char **)cr[ptc]) = "\x1b[0mDirectory %s has been created!\n";
        ptc++;
        *(cr + ptc) = malloc(sizeof(char *));
        *((char **)cr[ptc]) = *((char **)xr[ptx - 1]);
        ptc++;
        char buffer[strlen(*((char **)cr[0]))];
        sprintf(buffer, *((char **)cr[0]), *((char **)cr[1]));
        printf("%s", buffer);
        free(cr);
    }
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