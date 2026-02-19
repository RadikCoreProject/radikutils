import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoxPlotTest extends ApplicationFrame {

    public BoxPlotTest(String title) {
        super(title);
        JFreeChart chart = createChart(createDataset());
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private BoxAndWhiskerCategoryDataset createDataset() {
        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

        // Категория A
        List<Double> valuesA = Arrays.asList(2.0, 3.0, 3.0, 4.0, 5.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0);
        dataset.add(createBoxItem(valuesA), "Ряд 1", "Категория A");

        // Категория B
        List<Double> valuesB = Arrays.asList(15.0, 17.0, 18.0, 20.0, 21.0, 22.0, 24.0, 25.0, 26.0, 28.0, 30.0, 35.0);
        dataset.add(createBoxItem(valuesB), "Ряд 1", "Категория B");

        // Категория C
        List<Double> valuesC = Arrays.asList(50.0, 52.0, 55.0, 57.0, 60.0, 62.0, 65.0, 67.0, 70.0, 72.0, 75.0, 80.0, 85.0, 90.0);
        dataset.add(createBoxItem(valuesC), "Ряд 1", "Категория C");

        return dataset;
    }

    private BoxAndWhiskerItem createBoxItem(List<Double> values) {
        double[] array = values.stream().mapToDouble(Double::doubleValue).toArray();
        Arrays.sort(array);
        int n = array.length;

        double mean = Arrays.stream(array).average().orElse(0);
        double median = n % 2 == 0 ? (array[n/2 - 1] + array[n/2]) / 2 : array[n/2];
        double q1 = array[(int) Math.floor(n * 0.25)];
        double q3 = array[(int) Math.floor(n * 0.75)];
        double min = array[0];
        double max = array[n - 1];
        double iqr = q3 - q1;
        double lowerBound = q1 - 1.5 * iqr;
        double upperBound = q3 + 1.5 * iqr;

        // Определяем выбросы и границы усов без выбросов
        List<Double> outliers = new ArrayList<>();
        double minRegular = min;
        double maxRegular = max;
        for (double v : array) {
            if (v < lowerBound || v > upperBound) {
                outliers.add(v);
            } else {
                // для minRegular и maxRegular можно не трогать, они уже установлены
            }
        }
        // Корректируем границы усов (на самом деле они должны быть min и max без учёта выбросов)
        // В нашем упрощённом варианте оставим min и max как есть, outliers выделим отдельно
        // Но для корректности лучше использовать minRegular/maxRegular как минимальное и максимальное среди не-выбросов
        double minReg = min;
        double maxReg = max;
        for (double v : array) {
            if (v >= lowerBound && v <= upperBound) {
                minReg = Math.min(minReg, v);
                maxReg = Math.max(maxReg, v);
            }
        }

        return new BoxAndWhiskerItem(
            mean,           // среднее
            median,         // медиана
            q1,             // первый квартиль
            q3,             // третий квартиль
            minReg,         // минимальное без выбросов (конец нижнего уса)
            maxReg,         // максимальное без выбросов (конец верхнего уса)
            min,            // минимальное значение (включая выбросы)
            max,            // максимальное значение (включая выбросы)
            outliers        // список выбросов
        );
    }

    private JFreeChart createChart(BoxAndWhiskerCategoryDataset dataset) {
        var chart = ChartFactory.createBoxAndWhiskerChart(
            "Тестовый ящик с усами",   // заголовок
            "Категории",                // ось X
            "Значения",                  // ось Y
            dataset,                     // данные
            true                          // легенда
        );

        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setMeanVisible(false);
        chart.getCategoryPlot().setRenderer(renderer);
        return chart;
    }

    public static void main(String[] args) {
        BoxPlotTest demo = new BoxPlotTest("Box Plot Demo");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
