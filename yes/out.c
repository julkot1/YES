#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/syscall.h>
#define PTG_SIZE 300000
void **gr;void **xr;int main(){gr = malloc(PTG_SIZE * sizeof(void *));unsigned long ptg = 0;xr = malloc(16 * sizeof(void *));for (size_t i = 0; i < 16; i++)xr[i] = malloc(32 * sizeof(char));gr[ptg] = strdup("sfd");ptg++;{for(size_t i = 0; i <10; i++){*((size_t*)xr[1])=i;*((unsigned char *)xr[0]) = (0)==((*((int*)xr[1])%2));if(*((char *)xr[0]))continue;{char *buffer;asprintf(&buffer, "%i\n", *((int*)xr[1]));xr[0] = strdup(buffer);}*((int *)xr[0])=syscall(1,1,((char*)xr[0]),strlen(((char*)xr[0])));}}*((unsigned char*)xr[0]) = 4;{if(*((unsigned char*)xr[0])==0){xr[0]=strdup("a");}else if(*((unsigned char*)xr[0])==4){*((int *)xr[0])=syscall(1,1,"hello\n",6);}else if(*((unsigned char*)xr[0])==3){xr[0]=strdup("c");}}*((int *)xr[0])=syscall(1,1,((char*)xr[0]),1);for (size_t i = 0; i < PTG_SIZE; i++){gr[i] = malloc(0);free(*(gr+i));}free(gr);for (size_t i = 0; i < 16; i++){xr[i] = malloc(0);free(*(xr+i));}free(xr);return 0;}
