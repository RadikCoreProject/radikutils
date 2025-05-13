package org.radikutils.numeric;

public interface Nnet {
    long longValue();

    int intValue();

    byte byteValue();

    Nnet toBinary();

    Nnet cast(int base);
}
