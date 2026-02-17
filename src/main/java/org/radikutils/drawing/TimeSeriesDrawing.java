package org.radikutils.drawing;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.radikutils.parser.Parser;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class TimeSeriesDrawing<T extends Parser, P extends IntervalXYDataset> extends ApplicationFrame {
    private final String title;
    private final String xLabel;
    private final String yLabel;
    private final T parser;
    private final Dataset<T, P> dataset;
    private final boolean logarithmic;

    public TimeSeriesDrawing(String title, String xLabel, String yLabel, T parser, Dataset<T, P> dataset, boolean logarithmic) {
        super(title);
        this.title = title;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.parser = parser;
        this.dataset = dataset;
        this.logarithmic = logarithmic;
    }

    public void draw() {
        JFreeChart chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        setContentPane(chartPanel);
        pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setVisible(true);
    }

    private JFreeChart createChart() {
        JFreeChart chart = ChartFactory.createXYBarChart(
            title,
            xLabel,
            true,
            yLabel,
            dataset.run(parser),
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        chart.setBackgroundPaint(Color.white);
        XYPlot plot = chart.getXYPlot();

        // Настройка оси X как временной
        DateAxis dateAxis = new DateAxis(xLabel);
        dateAxis.setDateFormatOverride(new SimpleDateFormat("dd/MM/yyyy"));
        plot.setDomainAxis(dateAxis);

        // Настройка оси Y (логарифмическая или линейная)
        if (logarithmic) {
            LogarithmicAxis logAxis = new LogarithmicAxis(yLabel);
            logAxis.setAllowNegativesFlag(true);
            plot.setRangeAxis(logAxis);
        } else {
        }

        plot.setBackgroundPaint(new Color(212, 212, 248));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);

        return chart;
    }
}
