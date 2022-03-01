#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/syscall.h>
#define PTG_SIZE 300000
void **gr;void **xr;unsigned long ptg = 0;void ECHO(char * ar0);void ECHO(char * ar0){size_t pta = 1;*((unsigned long*)xr[0]) =strlen(((char*)ar0));*((int *)xr[0])=syscall(1,1,((char*)ar0),*((unsigned long*)xr[0]));}int main(){gr = malloc(PTG_SIZE * sizeof(void *));xr = malloc(16 * sizeof(void *));for (size_t i = 0; i < 16; i++)xr[i] = malloc(32 * sizeof(char));*((unsigned char *)xr[0]) = (2+2);{char *buffer;asprintf(&buffer, "%hhu\n", *((unsigned char*)xr[0]));xr[0] = strdup(buffer);}ECHO(strdup((char*)xr[0]));for (size_t i = 0; i < PTG_SIZE; i++){gr[i] = malloc(0);free(*(gr+i));}free(gr);for (size_t i = 0; i < 16; i++){xr[i] = malloc(0);free(*(xr+i));}free(xr);return 0;}
