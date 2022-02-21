#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/syscall.h>
#define PTG_SIZE 300000
void **gr;void **xr;char* FOO(unsigned char ar0,unsigned char ar1,char * ar2){size_t pta = 3;{char *buffer;asprintf(&buffer, "%hhu %hhu %s\n", ar0, ar1, ar2);xr[0] = strdup(buffer);}return ((char*)xr[0]);}int main(){gr = malloc(PTG_SIZE * sizeof(void *));unsigned long ptg = 0;xr = malloc(16 * sizeof(void *));for (size_t i = 0; i < 16; i++)xr[i] = malloc(32 * sizeof(char));xr[0]=strdup(FOO(2,2,"ef"));*((int *)xr[0])=syscall(1,1,((char*)xr[0]),strlen(((char*)xr[0])));for (size_t i = 0; i < PTG_SIZE; i++){gr[i] = malloc(0);free(*(gr+i));}free(gr);for (size_t i = 0; i < 16; i++){xr[i] = malloc(0);free(*(xr+i));}free(xr);return 0;}
