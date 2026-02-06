import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Urkop {
    public static void main(String[] args) {
        Random random = new Random();
        byte[] b = new byte[10000];
        random.nextBytes(b);
        BigInteger bigInteger = new BigInteger(b);
        System.out.println(bigInteger.pow(10));
    }
}


//        int s = sc.nextInt();
//        for (int i = 0; i < s; i++) {
//            long stick = sc.nextLong();
//            System.out.println((int) Math.sqrt(stick / 3f + 1.5));
//        }
