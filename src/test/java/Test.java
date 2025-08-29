import java.math.BigInteger;
import java.util.Scanner;

class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String atoms = scanner.next(); // число в обертке строки
        int H = scanner.nextInt();
        int He = scanner.nextInt();
        Double.POSITIVE_INFINITY

        if (H + He > 100) {
            System.out.print("Вселенная не существует");
            return;
        }
        atoms.matches()

        BigInteger atoms1 = new BigInteger(atoms);
        BigInteger atoms1precent = atoms1.divide(new BigInteger("100"));
        BigInteger ans = atoms1.multiply(new BigInteger(String.valueOf(100 - H - He)));
        System.out.println(ans);
    }
}
