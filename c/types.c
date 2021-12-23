#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main()
{

    void **array = malloc(sizeof(void *) * 5);
    *(array + 1) = malloc(sizeof(char));
    *((char *)array + 1) = 's';
    printf("%d\n", *((char *)array + 1));
    return 0;
}