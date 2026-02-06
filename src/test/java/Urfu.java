import java.util.*;

public class Urfu {
    public static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        pn();
    }

    public static void p1() {
        int t = 0;
        while (sc.nextInt() >= 0) t++;
        System.out.println(t);
    }

    public static void p2() {
        int t = 0;
        int a = sc.nextInt();
        while (a % 5 != 0) {
            t += a > 10 ? 1 : 0;
            a = sc.nextInt();
        }
        System.out.println(t);
    }

    public static void p3() {
        int t = 0;
        int a = sc.nextInt();
        while (9 < a && 100 > a) {
            t += a % 10 + a / 10;
            a = sc.nextInt();
        }
        System.out.println(t);
    }

    public static void pn() {
        int n = sc.nextInt();
        int[] arr = new int[n];
        int[] ans = new int[n];

        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();

        int idx = 0;
        for (int i = 0; i < n; i++) {
            if (arr[i] < 0) ans[idx++] = arr[i];
        }

        for (int i = 0; i < n; i++) {
            if (arr[i] >= 0) ans[idx++] = arr[i];
        }

        String k = Arrays.toString(ans);
        System.out.println(k.substring(1, k.length() - 1).replace(",", ""));
    }
}
