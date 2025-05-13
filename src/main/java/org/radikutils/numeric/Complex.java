package org.radikutils.numeric;

import org.jetbrains.annotations.NotNull;

// TODO: метод для поворота на градусы
public class Complex {
    private final double re;
    private final double im;

    public Complex(double real, double imag) {
        this.re = real;
        this.im = imag;
    }

    public Complex add(@NotNull Complex other) {
        return new Complex(this.re + other.re, this.im + other.im);
    }

    public Complex subtract(@NotNull Complex other) {
        return new Complex(this.re - other.re, this.im - other.im);
    }

    public Complex multiply(@NotNull Complex other) {
        double real = this.re * other.re - this.im * other.im;
        double imag = this.re * other.im + this.im * other.re;
        return new Complex(real, imag);
    }

    public double getRe() { return re; }
    public double getIm() { return im; }

    public double abs() {
        return Math.sqrt(re * re + im * im);
    }

    @Override
    public String toString() {
        return this.im >= 0 ? this.re + "+" + this.im + "i" : this.re + "" + this.im + "i";
    }
}
