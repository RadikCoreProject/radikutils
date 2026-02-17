import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double a = sc.nextDouble();
        double b = sc.nextDouble();
        double c = sc.nextDouble();
        double d = sc.nextDouble();

        double l = -100000;
        double r = 100000;
        double fl = f(l, a, b, c, d);
        double fr = f(r, a, b, c, d);

        while (fl * fr > 0) {
            l *= 2;
            r *= 2;
            fl = f(l, a, b, c, d);
            fr = f(r, a, b, c, d);
        }

        double eps = 1e-10;
        while (r - l > eps) {
            double mid = (l + r) / 2.0;
            double fMid = f(mid, a, b, c, d);
            if (a < 0 && fMid < 0 || a > 0 && fMid > 0) r = mid;
            else l = mid;
        }

        System.out.printf("%.7f\n", l);
    }

    private static double f(double x, double a, double b, double c, double d) {
        return a * x * x * x + b * x * x + c * x + d;
    }
}