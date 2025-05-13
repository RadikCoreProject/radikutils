package org.radikutils.numeric;


public class Bin implements Nnet {
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
    public Nnet toBinary() {
        return this;
    }

    @Override
    public Nnet cast(int base) {
        return this;
    }
}
