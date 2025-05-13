package org.radikutils.drawing;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.SwingWrapper;

public class Test {
    public static void main(String[] args) {
        // Создаем данные для графика
        double[] xData = new double[] { 0, 1, 2, 3, 4, 5 };
        double[] yData = new double[] { 0, 1, 4, 9, 16, 25 };

        // Создаем график
        XYChart chart = new XYChart(600, 400);
        chart.setTitle("Простой линейный график");
        chart.setXAxisTitle("X");
        chart.setYAxisTitle("Y");
        chart.addSeries("y = F(x)", xData, yData);

        // Отображаем график
        new SwingWrapper<>(chart).displayChart();
    }
}
