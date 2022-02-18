#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/syscall.h>
#define PTG_SIZE 300000
void **gr;void **xr;int main(){gr = malloc(PTG_SIZE * sizeof(void *));unsigned long ptg = 0;xr = malloc(16 * sizeof(void *));for (size_t i = 0; i < 16; i++)xr[i] = malloc(32 * sizeof(char));{char *buffer;scanf("%ms", &buffer);xr[2] = strdup(buffer);}gr[ptg] = strdup(((char*)xr[2]));ptg++;{short buffer;scanf("%hi", &buffer);*((short *)xr[2]) = buffer;}{char *buffer;asprintf(&buffer, "%s: %hi", ((char*)gr[ptg-1-0]), *((short*)xr[2]));xr[0] = strdup(buffer);}*((int *)xr[0])=syscall(1,1,((char*)xr[0]),strlen(((char*)xr[0])));for (size_t i = 0; i < PTG_SIZE; i++){gr[i] = malloc(0);free(*(gr+i));}free(gr);for (size_t i = 0; i < 16; i++){xr[i] = malloc(0);free(*(xr+i));}free(xr);return 0;}
