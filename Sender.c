#include <stdio.h>
#include <math.h>
#include <string.h>

int main() {
    printf("Enter the data bits: ");
    char d[100];
    scanf("%s", d);

    int len = strlen(d);
    char data[len];
    strcpy(data, d);
    int r = 0;
    int c = 0;
    int ch = 0;
    int j = 0;
    int h[100] = {0};

    while ((len + r + 1) > pow(2, r)) {
        r++;
    }

    for (int i = 0; i < (r + len); i++) {
        int p = pow(2, c);
        if (p == (i + 1)) {
            h[i] = 0;
            c++;
        } else {
            h[i] = data[j] - '0';
            j++;
        }
    }

    for (int parity = 0; parity < (r + len); parity++) {
        int ph = pow(2, ch);
        if (ph == (parity + 1)) {
            int startIndex = ph - 1;
            int i = startIndex;
            int toXor[100];
            int toXorIdx = 0;

            while (i < (r + len)) {
                for (int idx = i; idx < i + ph; idx++) {
                    if (idx < (r + len)) {
                        toXor[toXorIdx] = h[idx];
                        toXorIdx++;
                    }
                }
                i += 2 * ph;
            }

            for (int z = 1; z < toXorIdx; z++) {
                h[startIndex] = h[startIndex] ^ toXor[z];
            }
            ch++;
        }
    }

    for (int i = 0; i < (r + len); i++) {
        printf("%d", h[i]);
    }

    printf("\n");
    return 0;
}

