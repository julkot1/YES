#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main()
{
    char *arg1 = "%d %d sd";
    int arg2 = 200;
    int arg3 = 200;
    //
    char buffer[strlen(arg1)];
    sprintf(buffer, arg1, arg2, arg3);
    printf("%s", buffer);
    do
    {
        printf("sd\n");
    } while (0);
    printf("sdq");
    char *c = "pwd";
    system("pwd");
    return 0;
}