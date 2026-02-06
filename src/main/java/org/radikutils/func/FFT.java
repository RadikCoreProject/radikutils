package org.radikutils.func;

import org.jetbrains.annotations.NotNull;
import org.radikutils.drawing.DrawingBase;
import org.radikutils.numeric.Complex;
import org.radikutils.plets.Duplet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FFT {
    private static final double PI = Math.PI;
    private static final long DOTS = (long) Math.pow(2, 10);
    private static final Duplet<Integer, Long> CENS = new Duplet<>(1, DOTS / 2);

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

    public static double func(double i) {
        return i % 200;
    }

    public static double hanningWindow(int i, int DOTS) {
        return 0.5 * (1 - Math.cos(2 * Math.PI * i / (DOTS - 1)));
    }

    public static void calc() {
        DrawingBase ch1 = new DrawingBase();
        DrawingBase ch2 = new DrawingBase();

        ArrayList<Complex> input = new ArrayList<>();

        for (int i = 0; i < DOTS; i++) {
            double window = hanningWindow(i, (int) DOTS);
//            input.add(new Complex(func(i) * window, 0));
            input.add(new Complex(func(i), 0));
        }

        ch1.setTitle("FFT Transform");
        ch1.setLegend("X", "Y");
        ch2.setTitle("FFT Transform");
        ch2.setLegend("X", "Y");

        List<Complex> output = fft(input);

        ArrayList<Double> xData;
        ArrayList<Double> yData;

//        xData = new ArrayList<>();
//        yData = new ArrayList<>();
//        for (int i = CENS.getType(); i < CENS.getParametrize(); i++) {
//            xData.add((double) i);
//            yData.add(func(i));
//        }
//        ch1.custom(xData, yData, "y=sin(x)");
//        ch1.runChart();

        xData = new ArrayList<>();
        yData = new ArrayList<>();
        double maxAmplitude = 0;
        for (int i = CENS.getType(); i < CENS.getParametrize(); i++) {
            xData.add((double) i);
            yData.add(output.get(i).getRe());
            maxAmplitude = Math.max(maxAmplitude, output.get(i).abs());
        }

        for (int i = 0; i < yData.size(); i++) {
            yData.set(i, yData.get(i) / maxAmplitude);
        }
        ch2.custom(xData, yData, "y=F(1 if i % 1000 > 900)");
        ch2.runChart();
    }
}
