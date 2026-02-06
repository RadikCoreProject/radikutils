package org.radikutils.numeric;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.HashMap;

public class StaticBases {
    private static final HashMap<String, Integer> ten2base = new HashMap<>();

    static { // записываем аннотации
        ten2base.put("A", 10);
        ten2base.put("B", 11);
        ten2base.put("C", 12);
        ten2base.put("D", 13);
        ten2base.put("E", 14);
        ten2base.put("F", 15);
        ten2base.put("G", 16);
        ten2base.put("H", 17);
        ten2base.put("I", 18);
        ten2base.put("J", 19);
        ten2base.put("K", 20);
        ten2base.put("L", 21);
        ten2base.put("M", 22);
        ten2base.put("N", 23);
        ten2base.put("O", 24);
        ten2base.put("P", 25);
        ten2base.put("Q", 26);
        ten2base.put("R", 27);
        ten2base.put("S", 28);
        ten2base.put("T", 29);
        ten2base.put("U", 30);
        ten2base.put("V", 31);
        ten2base.put("W", 32);
        ten2base.put("X", 33);
        ten2base.put("Y", 34);
        ten2base.put("Z", 35);
    }

    public static @NotNull String ten2base(long num, int base) {
        if (num == 0) {
            return "0";
        }

        StringBuilder result = new StringBuilder();
        boolean isNegative = num < 0;
        num = Math.abs(num);

        while (num > 0) {
            long r = num % base;
            result.append(r >= 10 ? (char) ('A' + r - 10) : r);
            num /= base;
        }
        if (isNegative) {
            result.append('-');
        }

        return result.reverse().toString();
    }

    public static @NotNull String ten2base(BigInteger num, int base) {
        if (num.equals(BigInteger.ZERO)) {
            return "0";
        }

        BigInteger b = new BigInteger(String.valueOf(base));
        StringBuilder result = new StringBuilder();
        boolean isNegative = num.signum() == -1;
        num = num.abs();

        while (!num.equals(BigInteger.ZERO)) {
            int r = num.remainder(b).intValue();
            result.append(r >= 10 ? (char) ('A' + r - 10) : r);
            num = num.divide(b);
        }
        if (isNegative) {
            result.append('-');
        }

        return result.reverse().toString();
    }

    public static long base2ten(@NotNull String num, int base) {
        if (num.equals("0")) {
            return 0;
        }

        long result = 0;
        boolean isNegative = false;
        if (num.startsWith("-")) {
            isNegative = true;
            num = num.substring(1);
        }
        int len = num.length() - 1;

        for (char i : num.toCharArray()) {
            try {
                result += (int) (Integer.parseInt(String.valueOf(i)) * Math.pow(base, len--));
            } catch (Exception e) {
                result += (int) (ten2base.get(String.valueOf(i)) * Math.pow(base, len--));
            }
        }
        if (isNegative) {
            result = -result;
        }
        return result;
    }

    public static double two2ten(@NotNull String twos) {
        String[] two = twos.split("\\.");

        String s = two[0];
        String e = two.length == 1 ? "0" : two[1];
        double ans = 0;
        int counter = 0;

        while (!s.isEmpty() && !s.equals("0")) {
            if (s.endsWith("1")) ans += 1L << counter;
            counter++;
            s = s.substring(0, s.length() - 1);
        }

        counter = 0;
        while (!e.isEmpty() && !e.equals("0")) {
            if (e.startsWith("1")) ans += 1.0 / (2L << counter);
            counter++;
            e = e.substring(1);
        }

        return ans;
    }

    public static String ten2two(@NotNull String d) {
        String[] n = d.split("\\.");
        StringBuilder ans = new StringBuilder(String.valueOf(base2ten(n[0], 2)));
        if (n.length == 1) System.out.println(ans.append(".0"));
        else {
            ans.append(".");
            double f = Double.parseDouble("0." + n[1]);
            for (int i = 0; i < 32; i++) {
                if (f == 0) break;
                f *= 2;
                ans.append((int) f);
                f %= 1;
            }
            System.out.print(ans);
        }
        return ans.toString();
    }
}
