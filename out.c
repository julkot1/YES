#include <stdio.h>
#include <stdlib.h>
#include<unistd.h>
#include <string.h>
#define PTG_SIZE 300000
#define PTX_SIZE 50000
int main()
{void **gr = malloc(PTG_SIZE * sizeof(void *));void **xr = malloc(PTX_SIZE * sizeof(void *));int ptg = 0, ptx = 0;{void **cr = malloc(1 * sizeof(void *));int ptc=0;*(cr + ptc) = malloc(sizeof(unsigned char));*((unsigned char *)cr[ptc]) = 6; ptc++;gr[ptg] = malloc(sizeof(unsigned char));*((unsigned char*)gr[ptg]) = *((unsigned char*)cr[0]);ptg++;free(cr);}{void **cr = malloc(2 * sizeof(void *));int ptc=0;do{void **cr = malloc(2 * sizeof(void *));int ptc=0;*(cr + ptc) = malloc(sizeof(unsigned char));*((unsigned char *)cr[ptc]) = 5; ptc++;*(cr + ptc) = malloc(sizeof(unsigned char));*((unsigned char *)cr[ptc]) = 6; ptc++;xr[ptx] = malloc(sizeof(unsigned char));*((unsigned char *)xr[ptx]) = *((unsigned char*)cr[0]) > *((unsigned char*)cr[1]);ptx++;free(cr);}while(0);*(cr + ptc) = malloc(sizeof(unsigned char));*((unsigned char *)cr[ptc]) = *((unsigned char*)xr[ptx - 1]); ptc++;if(!*((int*)cr[0])){*(cr + ptc) = malloc(sizeof(char *));*((char * *)cr[ptc]) = "not"; ptc++;printf("%s", *((char **) cr[1]));exit(EXIT_FAILURE);}free(cr);}for (int i = 0; i < PTX_SIZE; i++){free(*(xr+i));}free(xr);for (int i = 0; i < PTG_SIZE; i++){free(*(gr+i));}free(gr);return 0;}