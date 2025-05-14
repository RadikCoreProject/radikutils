package org.radikutils.numeric;


import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class Bin extends Number implements Nnet {
    public Bin(long value) {
        bin = String.valueOf(value);
    }

    public Bin(int value) {
        bin = String.valueOf(value);
    }

    public Bin(String bin) {
        this.bin = bin;
    }

    private String bin;

    @Override
    public int intValue() {
        return Integer.parseInt(bin);
    }

    @Override
    public byte byteValue() {
        return Byte.parseByte(bin);
    }

    @Override
    public long longValue() {
        return Long.parseLong(bin);
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
    }

    @Override
    public Nnet toBinary() {
        return this;
    }

    @Override
    public Nnet cast(int base) {
        if (base == 2) return this;
        double v = Math.log(base) / Math.log(2);
        int l = this.bin.length();
        if ((int) (v) == v) {
            ArrayList<String> s = new ArrayList<>();
            while (s.size() < l / v) {
                if (s.size() + 1 * v >= l) {
                    s.add(this.bin.substring(0, (int) (l - s.size() * v)));
                    break;
                }
                s.add(this.bin.substring((int) (l - (s.size() + 1) * v), (int) (l - s.size() * v)));
            }
            s.reversed();
            return String.join(" ", s);
        }
        return this;
    }

    @Override
    public String toString() {
        return bin;
    }
}
