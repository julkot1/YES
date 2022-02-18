#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/syscall.h>
#define PTG_SIZE 300000
void **gr;void **xr;int main(){gr = malloc(PTG_SIZE * sizeof(void *));unsigned long ptg = 0;xr = malloc(16 * sizeof(void *));for (size_t i = 0; i < 16; i++)xr[i] = malloc(32 * sizeof(char));gr[ptg] = malloc(sizeof(int));*((int*)gr[ptg]) = 5000000000;ptg++;{for(size_t i = 0; i <*((int*)gr[ptg-1-0]); i++){*((size_t*)xr[1])=i;*((int *)xr[0]) = (0)==((*((int*)gr[ptg-1-0])%(1+*((int*)xr[1]))));{if(*((unsigned char*)xr[0])){*((int *)xr[0])=syscall(1,1,"is\n",3);}}}}for (size_t i = 0; i < PTG_SIZE; i++){gr[i] = malloc(0);free(*(gr+i));}free(gr);for (size_t i = 0; i < 16; i++){xr[i] = malloc(0);free(*(xr+i));}free(xr);return 0;}
