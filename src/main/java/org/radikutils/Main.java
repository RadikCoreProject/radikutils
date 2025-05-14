package org.radikutils;

import org.radikutils.drawing.DrawingBase;
import org.radikutils.numeric.Complex;
import org.radikutils.plets.Duplet;

import java.util.ArrayList;
import java.util.List;

import static org.radikutils.drawing.DrawingBase.*;
import static org.radikutils.func.FFT.fft;
import static org.radikutils.func.FFT.ifft;

public class Main {
    private static final long DOTS = (long) Math.pow(2, 10);
    private static final Duplet<Integer, Long> CENS = new Duplet<>(1, DOTS / 2);

    public static void main(String[] args) {
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

    private static double func(double i) {
        return i % 200;
    }

    public static double hanningWindow(int i, int DOTS) {
        return 0.5 * (1 - Math.cos(2 * Math.PI * i / (DOTS - 1)));
    }
}
