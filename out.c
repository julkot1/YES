#include <stdio.h>
#include <stdlib.h>
#include<unistd.h>
#include <string.h>
#define PTG_SIZE 300000
#define PTX_SIZE 50000
int main()
{void **gr = malloc(PTG_SIZE * sizeof(void *));void **xr = malloc(PTX_SIZE * sizeof(void *));int ptg = 0, ptx = 0;{void **cr = malloc(2 * sizeof(void *));int ptc=0;*(cr + ptc) = malloc(sizeof(unsigned char));*((unsigned char *)cr[ptc]) = 0b11001000; ptc++;*(cr + ptc) = malloc(sizeof(unsigned char));*((unsigned char *)cr[ptc]) = 0b10111000; ptc++;xr[ptx] = malloc(sizeof(int));*((int *)xr[ptx]) = *((unsigned char*)cr[0]) & *((unsigned char*)cr[1]);ptx++;free(cr);};{void **cr = malloc(2 * sizeof(void *));int ptc=0;*(cr + ptc) = malloc(sizeof(char *));*((char * *)cr[ptc]) = "%d\n sdsd \n"; ptc++;*(cr + ptc) = malloc(sizeof(int));*((int *)cr[ptc]) = *((int*)xr[ptx - 1]); ptc++;char buffer[strlen(*((char **)cr[0]))];sprintf(buffer, *((char **)cr[0]), *((int*)cr[1]));printf("%s", buffer);free(cr);};for (int i = 0; i < PTX_SIZE; i++){free(*(xr+i));}free(xr);for (int i = 0; i < PTG_SIZE; i++){free(*(gr+i));}free(gr);return 0;}