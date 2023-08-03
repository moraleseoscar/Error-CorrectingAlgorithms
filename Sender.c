#include <stdio.h>
#include <math.h>
#include <string.h>


char* xor_binary_strings(const char* str1, const char* str2) {
    static char result[64];
    int len1 = strlen(str1);
    int len2 = strlen(str2);
    int max_len = len1 > len2 ? len1 : len2;


    while (len1 < max_len) {
        result[len1] = '0';
        len1++;
    }

    while (len2 < max_len) {
        result[len2] = '0';
        len2++;
    }

    for (int i = 0; i < max_len; i++) {
        result[i] = (str1[i] != str2[i]) ? '1' : '0';
    }

    result[max_len] = '\0';
    return result;
}
void crc_emisor(const char* message) {
    int len = strlen(message);
    char char_list[len + 32];
    const char* crc32 = "100000100110000010001110110110111";

    for (int i = 0; i < len; i++) {
        char_list[i] = message[i];
    }

    for (int i = 0; i < 32; i++) {
        char_list[len + i] = '0';
    }

    char A[34]; 
    strncpy(A, char_list, 33);
    A[33] = '\0'; 
    memmove(char_list, char_list + 33, len + 32 - 33);
    char_list[len + 32 - 33] = '\0'; 

    char* result = xor_binary_strings(A, crc32);

    while (strlen(char_list) > 0) {

        printf("trama: %s\n", char_list);
        printf("O: %s\n", result);
        if (result[0] = '0'){
            memmove(result, result+1, strlen(result));
            char agregar = char_list[0];
            memmove(char_list, char_list+1, strlen(char_list));
            int lent = strlen(result);
            result[lent] = agregar;
            result[lent+1] = '\0'; 
        }
        if (result[0] = '1'){
            result  = xor_binary_strings(result, crc32);
        }
    }

    printf("Emisor: %s%s\n", message, result);

}


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

    // CRC32

    
    const char* message = "110101011";

    crc_emisor(message);

    return 0;
}

