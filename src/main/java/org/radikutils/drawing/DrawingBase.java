package org.radikutils.drawing;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.util.List;

public class DrawingBase {
    private final XYChart CHART = new XYChartBuilder().width(800).height(600).build();
    public void custom(List<? extends Number> xData, List<? extends Number> yData, String subTitle) {
        CHART.addSeries(subTitle, xData, yData);
    }

    public void setLegend(String l1, String l2) {
        CHART.setXAxisTitle(l1);
        CHART.setYAxisTitle(l2);
    }

    public void setTitle(String title) {
        CHART.setTitle(title);
    }

    public void runChart() {
        new SwingWrapper<>(CHART).displayChart();
    }

    public void removeChart(String name) {
        CHART.removeSeries(name);
    }
}
