package org.radikutils.numeric;

import java.util.HashMap;

public class Based extends Number implements Nnet {
    public Based(int base, long decimal) {
        super();
        this.base = base;
        this.num = StaticBases.ten2base(decimal, base);
    }

    public Based(int base, String decimal) {
        super();
        this.base = base;

    }

    private final int base;
    private final String num;

    @Override
    public int intValue() {
        return 0;
    }

    @Override
    public Nnet toBinary() {
        return null;
    }

    @Override
    public Nnet cast(int base) {
        return null;
    }

    @Override
    public long longValue() {
        return 0;
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
    public String toString() {
    }
}
