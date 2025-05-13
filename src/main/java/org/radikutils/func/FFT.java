package org.radikutils.func;

import org.jetbrains.annotations.NotNull;
import org.radikutils.numeric.Complex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FFT {
    private static final double PI = Math.PI;

    // TODO: do not binary FFT
    public static @NotNull List<Complex> fft(@NotNull List<Complex> a, boolean inverse) {
        int n = a.size();
        if ((n & (n - 1)) != 0) {
            throw new FFTException("NOT a binary FFT");
        }

        if (n == 1) return new ArrayList<>(a);
        List<Complex> a0 = new ArrayList<>(n / 2);
        List<Complex> a1 = new ArrayList<>(n / 2);
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) a0.add(a.get(i));
            else a1.add(a.get(i));
        }

        a0 = fft(a0, inverse);
        a1 = fft(a1, inverse);

        int halfN = n / 2;
        List<Complex> result = new ArrayList<>(Collections.nCopies(n, null));
        double sign = inverse ? 1 : -1;

        for (int i = 0; i < halfN; i++) {
            Complex w = new Complex(
                    Math.cos(2 * PI * i / n),
                    sign * Math.sin(2 * PI * i / n)
            );

            Complex even = a0.get(i);
            Complex odd = a1.get(i).multiply(w);
            result.set(i, even.add(odd));
            result.set(i + halfN, even.subtract(odd));
        }
        if (inverse) {
            for (int i = 0; i < n; i++) {
                Complex scaled = result.get(i).multiply(new Complex(1.0 / Math.log(n), 0));
                result.set(i, scaled);
            }
        }
        return result;
    }

    public static @NotNull List<Complex> ifft(List<Complex> a) {
        return fft(a, true);
    }

    public static @NotNull List<Complex> fft(List<Complex> a) {
        return fft(a, false);
    }
}
