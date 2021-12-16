#include <stdio.h>
#include <stdlib.h>
#define GR_SIZE 300000
#define XR_SIZE 50000
#define SHORT_SIZE 4
enum types
{
    CHAR,
    STR,
    BOOL,
    SHORT,
    INT,
    LONG,
    DOUBLE
};
struct type
{
    int type;
    char *val;
};
short getShort(char *value)
{
    short i = (*value << 4) + *(value + 1);
    return i;
}
int main()
{

    int *gr = (int *)malloc(GR_SIZE * sizeof(int));
    int *xr = (int *)malloc(XR_SIZE * sizeof(int));
    int ptc = 0;
    struct type s;

    s.val = (char *)malloc(8 * sizeof(char));
    *(s.val) = 0;
    *(s.val + 1) = 65;
    printf("%i\n", getShort(s.val));
    {
        int a = 4;
        {
            printf("%i\n", a);
        }
    }
    int ptr = 0, ptx = 0;
    // PUSH
    {
        *(gr + ptr) = 34;
        ptr++;
    }
    // PUSH
    *(gr + ptr) = 3;
    ptr++;
    // DROP
    *(gr + ptr) = 0;
    ptr--;
    // ADD ptr & ptr+1
    *(xr + ptx) = *(gr + ptr - 1) + *(gr + ptr - 2);
    ptx++;
    free(gr);
    free(xr);
    return 0;
}