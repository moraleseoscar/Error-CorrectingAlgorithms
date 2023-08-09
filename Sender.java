import java.util.Scanner;

public class Sender {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the data bits: ");
        String inputData = scanner.next();

        int len = inputData.length();
        char[] data = new char[len];
        inputData.getChars(0, len, data, 0);
        int r = 0;
        int c = 0;
        int ch = 0;
        int j = 0;
        int[] h = new int[100];

        while ((len + r + 1) > Math.pow(2, r)) {
            r++;
        }

        for (int i = 0; i < (r + len); i++) {
            int p = (int) Math.pow(2, c);
            if (p == (i + 1)) {
                h[i] = 0;
                c++;
            } else {
                h[i] = data[j] - '0';
                j++;
            }
        }

        for (int parity = 0; parity < (r + len); parity++) {
            int ph = (int) Math.pow(2, ch);
            if (ph == (parity + 1)) {
                int startIndex = ph - 1;
                int i = startIndex;
                int[] toXor = new int[100];
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
            System.out.print(h[i]);
        }

        System.out.println();
    }
}
