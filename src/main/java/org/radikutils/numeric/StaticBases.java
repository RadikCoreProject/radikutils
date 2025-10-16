package org.radikutils.numeric;

import org.jetbrains.annotations.NotNull;

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
        } // если число - 0 то возвращаем 0

        StringBuilder result = new StringBuilder(); // используем стринг билдер (изменение строки в цикле)
        boolean isNegative = num < 0; // проверяем на отрицательность
        num = Math.abs(num);

        while (num > 0) {
            long r = num % base; // берем остаток от деления на основание
            result.append(r >= 10 ? (char) ('A' + r - 10) : r); // если остаток больше 9, то берем букву с помощью char-a
            num /= base; // делим число
        }
        if (isNegative) {
            result.append('-'); // если отрицательное добавляем минус
        }

        return result.reverse().toString(); // реверсируем строку (тк начинаем от меньших значений)
    }

    public static long base2ten(@NotNull String num, int base) {
        // если число - 0, то возвращаем 0
        if (num.equals("0")) {
            return 0;
        }

        long result = 0;
        boolean isNegative = false;
        if (num.startsWith("-")) { // если число начинается с -, значит оно отрицательное
            isNegative = true;
            num = num.substring(1);
        }
        int len = num.length() - 1;

        for (char i : num.toCharArray()) { // проходимся по каждому символу
            try {
                result += (int) (Integer.parseInt(String.valueOf(i)) * Math.pow(base, len--));
            } catch (Exception e) { // если это НЕ ЧИСЛО, (к примеру буквы для оснований больших 10)
                result += (int) (ten2base.get(String.valueOf(i)) * Math.pow(base, len--)); // берем из списка аннотаций
            }
        }
        if (isNegative) {
            result = -result; // делаем число отрицательным если надо
        }
        return result;
    }
}
