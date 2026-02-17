package org.radikutils.drawing;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.radikutils.parser.Parser;

import java.awt.*;

public class BarDrawing<T extends Parser, P extends CategoryDataset> extends ApplicationFrame {
    public final String title;
    public final String category;
    public final String value;
    public final T parser;
    public final Dataset<T, P> dataset;
    public final boolean logarithmic;

    public BarDrawing(String title, String c, String v, T parser, Dataset<T, P> dataset, boolean logarithmic) {
        super(title);
        this.title = title;
        this.category = c;
        this.value = v;
        this.parser = parser;
        this.dataset = dataset;
        this.logarithmic = logarithmic;
    }

    public void draw() {
        JFreeChart chart = createChart(dataset.run(parser), title, category, value);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(480, 320));
        setContentPane(chartPanel);
        pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setVisible(true);
    }

    public void draw(CategoryDataset dataset) {
        JFreeChart chart = createChart(dataset, title, category, value);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(480, 320));
        setContentPane(chartPanel);
        pack();
        RefineryUtilities.centerFrameOnScreen(this);
        setVisible(true);
    }

    public JFreeChart createChart(CategoryDataset dataset, String title, String c, String v) {
        final JFreeChart chart = ChartFactory.createBarChart(
            title,
            c,
            v,
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );

        chart.setBackgroundPaint(Color.white);

        CategoryPlot plot = chart.getCategoryPlot();

        if (logarithmic) {
            LogarithmicAxis logAxis = new LogarithmicAxis(v);
            logAxis.setAllowNegativesFlag(true);
            plot.setRangeAxis(logAxis);
        } else {
            NumberAxis axis = (NumberAxis) plot.getRangeAxis();
            axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            axis.setUpperMargin(0.15);
        }

        plot.setBackgroundPaint(new Color(212, 212, 248));
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);


        CategoryAxis axis_d = plot.getDomainAxis();
        axis_d.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        return chart;
    }
}
