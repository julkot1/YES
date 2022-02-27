#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/syscall.h>
#define PTG_SIZE 300000
void **gr;void **xr;unsigned long ptg = 0;unsigned long FOO(char * ar0);unsigned long FOO(char * ar0){size_t pta = 1;{char *buffer;asprintf(&buffer, "%s\n", ((char*)ar0));xr[0] = strdup(buffer);}xr[4] = strdup(((char*)xr[0]));*((unsigned long*)xr[0]) =strlen(((char*)xr[0]));*((int *)xr[0])=syscall(1,1,((char*)xr[4]),*((unsigned long*)xr[0]));return *((unsigned long*)xr[0]);}int main(){gr = malloc(PTG_SIZE * sizeof(void *));xr = malloc(16 * sizeof(void *));for (size_t i = 0; i < 16; i++)xr[i] = malloc(32 * sizeof(char));FOO("dfdfd");for (size_t i = 0; i < PTG_SIZE; i++){gr[i] = malloc(0);free(*(gr+i));}free(gr);for (size_t i = 0; i < 16; i++){xr[i] = malloc(0);free(*(xr+i));}free(xr);return 0;}
