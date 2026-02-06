import java.util.Scanner;
import java.util.function.Function;

public class Test {
    static Function<Integer, String> f = String::valueOf;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int a = sc.nextInt();
        int length = (int) (Math.log(a) / Math.log(2));
        int ans = a;

        for (int i = 0; i < length; i++) {
            int t = a & 1;
            a = t == 1 ? (a >> 1) | (1 << length) : a >> 1;
            ans = Math.max(ans, a);
        }

        System.out.println(ans);
    }
}