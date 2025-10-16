import java.util.Scanner;

    public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int k = sc.nextInt();
        for (int i = 0; i < k; i++) {
            long a = sc.nextLong();
            long b = sc.nextLong();
            long c = sc.nextLong();
            long d = sc.nextLong();
            if (solve(a, b, c, d)) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        }
    }

    private static boolean solve(long a, long b, long c, long d) {
        while (a != 0 && b != 0) {
            if (a < b) {
                long t = a;
                a = b;
                b = t;
                if (a == c && b == d) {
                    return true;
                }
                continue;
            }
            if (a == c && b == d) {
                return true;
            }
            long r = a % b;
            if (d == b) {
                if (c <= a && c >= r + b && c % b == r) {
                    return true;
                }
            }
            if (c == r && d == b) {
                return true;
            }
            a = b;
            b = r;
        }
        if (a == 0) {
            return (c == 0 && d == b) || (c == b && d == 0);
        } else {
            return (c == a && d == 0) || (c == 0 && d == a);
        }
    }
}