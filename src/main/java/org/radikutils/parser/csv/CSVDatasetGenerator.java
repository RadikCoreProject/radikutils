package org.radikutils.parser.csv;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.radikutils.drawing.Dataset;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public final class CSVDatasetGenerator {
    public static org.radikutils.drawing.Dataset<CSVParser, XYSeriesCollection> genTimeDataset(String dateColumn, String valueColumn, CSVParser.DisplayCondition condition) {
        return genTimeDataset(dateColumn, valueColumn, s -> s, condition);
    }

    public static org.radikutils.drawing.Dataset<CSVParser, XYSeriesCollection> genTimeDataset(String dateColumn, String valueColumn, CSVParser.CountingCondition cou, CSVParser.DisplayCondition condition) {
        return parser -> {
            String[] dates = parser.mass.get(dateColumn);
            String[] values = parser.mass.get(valueColumn);

            Map<String, Map<String, Double>> dataByCategory = new LinkedHashMap<>();

            for (int i = 0; i < dates.length; i++) {
                String date = dates[i];
                double val = Double.parseDouble(values[i]);
                dataByCategory.computeIfAbsent(condition.cond(date, val), k -> new LinkedHashMap<>()).merge(cou.cond(date), val, Double::sum);
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (Map.Entry<String, Map<String, Double>> entry : dataByCategory.entrySet()) {
                String categoryName = entry.getKey();
                Map<String, Double> sumsByDate = entry.getValue();

                XYSeries series = new XYSeries(categoryName);
                sumsByDate.forEach((dateStr, sum) -> {
                    try {
                        long millis = sdf.parse(dateStr).getTime();
                        series.add(millis, sum);
                    } catch (ParseException ignored) {}
                });

                dataset.addSeries(series);
            }
            return dataset;
        };
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column) {
        return genDataset(row, column, val -> 1);
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column, CSVParser.DrawCondition condition) {
        return genDataset(row, column, condition, null, null);
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column, String condition, CSVParser.CountingCondition condition1) {
        return genDataset(row, column, val -> 1, t -> condition, condition1);
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column, CSVParser.CountingCondition condition, String condition1) {
        return genDataset(row, column, val -> 1, condition, t -> condition1);
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column, CSVParser.CountingCondition condition, CSVParser.CountingCondition condition1) {
        return genDataset(row, column, val -> 1, condition, condition1);
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column, CSVParser.DrawCondition condition, CSVParser.CountingCondition condition1, CSVParser.CountingCondition condition2) {
        return parser1 -> {
            boolean c = condition.type() != null;
            boolean n = condition2 != null;
            boolean p = condition2 != null;
            DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
            Map<String, String[]> mass = parser1.mass;

            String[] first = mass.get(column);
            String[] second = mass.get(row);
            String[] third = new String[first.length];

            if (c) third = mass.get(condition.type());
            Map<String, Map<String, Integer>> parse = new LinkedHashMap<>();
            for (int i = 0; i < first.length; i++) {
                String d = n ? condition2.cond(second[i]) : second[i];
                String th = third[i];
                String t = p ? condition1.cond(first[i]) : first[i];
                parse.computeIfAbsent(d, k -> new LinkedHashMap<>()).merge(t, condition.cond(th), Integer::sum);
            }

            List<String> sortedRowKeys = new ArrayList<>(parse.keySet());
            Collections.sort(sortedRowKeys);
            for (String rowKey : sortedRowKeys) {
                Map<String, Integer> colMap = parse.get(rowKey);
                List<String> sortedColKeys = new ArrayList<>(colMap.keySet());
                Collections.sort(sortedColKeys);
                for (String colKey : sortedColKeys) {
                    dataset1.addValue(colMap.get(colKey), rowKey, colKey);
                }
            }
            return dataset1;
        };
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column, CSVParser.DisplayCondition rowCond, CSVParser.DisplayCondition columnCond) {
        return parser -> {
            String[] dates = parser.mass.get(row);
            String[] values = parser.mass.get(column);

            Map<String, Map<String, Double>> sumsByRow = new LinkedHashMap<>();

            for (int i = 0; i < dates.length; i++) {
                String date = dates[i];
                double val = Double.parseDouble(values[i]);
                String rowKey = rowCond.cond(date, val);
                String colKey = columnCond.cond(date, val);

                sumsByRow.computeIfAbsent(rowKey, k -> new LinkedHashMap<>()).merge(colKey, val, Double::sum);
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            sumsByRow.forEach((rowName, colMap) ->
                colMap.forEach((colName, sum) ->
                    dataset.addValue(sum, rowName, colName)
                )
            );

            return dataset;
        };
    }

    public static Dataset<CSVParser, BoxAndWhiskerCategoryDataset> genBoxDataset(String category, String group, String val, boolean sumValues) {
        return parser -> {
            String[] categories = parser.mass.get(category);
            String[] groups = group != null ? parser.mass.get(group) : null;
            String[] values = val != null ? parser.mass.get(val) : null;

            Map<String, List<Double>> data = new LinkedHashMap<>();

            if (group == null) {
                if (val == null)
                    throw new IllegalArgumentException("val must be provided when group is null");
                for (int i = 0; i < categories.length; i++) {
                    String cat = categories[i];
                    double value = Double.parseDouble(values[i]);
                    data.computeIfAbsent(cat, k -> new ArrayList<>()).add(value);
                }
            } else {
                Map<String, Map<String, Double>> aggregated = new HashMap<>();
                for (int i = 0; i < categories.length; i++) {
                    String cat = categories[i];
                    String grp = groups[i];
                    double add = val != null && sumValues ? Double.parseDouble(values[i]) : 1.0;
                    aggregated.computeIfAbsent(cat, k -> new HashMap<>()).merge(grp, add, Double::sum);
                }
                for (Map.Entry<String, Map<String, Double>> entry : aggregated.entrySet()) {
                    String cat = entry.getKey();
                    Collection<Double> vals = entry.getValue().values();
                    data.put(cat, new ArrayList<>(vals));
                }
            }

            DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
            for (Map.Entry<String, List<Double>> entry : data.entrySet()) {
                String c = entry.getKey();
                List<Double> list = entry.getValue();
                double[] array = list.stream().mapToDouble(Double::doubleValue).toArray();
                BoxAndWhiskerItem item = createItem(array);
                dataset.add(item, "Распределение", c);
            }
            return dataset;
        };
    }

    private static BoxAndWhiskerItem createItem(double[] values) {
        if (values.length == 0) {
            return new BoxAndWhiskerItem(0,0,0,0,0,0,0,0, new ArrayList<>());
        }
        Arrays.sort(values);
        double mean = Arrays.stream(values).average().getAsDouble();
        double median = values[values.length / 2];
        double q1 = values[values.length / 4];
        double q3 = values[values.length * 3 / 4];
        double min = values[0];
        double max = values[values.length - 1];
        double iqr = q3 - q1;
        double l = q1 - 1.5 * iqr;
        double u = q3 + 1.5 * iqr;

        List<Double> outliers = new ArrayList<>();
        double minr = min;
        double maxr = max;
        for (double v : values) {
            if (v < l || v > u) outliers.add(v);
        }
        for (double v : values) {
            if (v >= l && v < minr) minr = v;
            if (v <= u && v > maxr) maxr = v;
        }
        return new BoxAndWhiskerItem(mean, median, q1, q3, minr, maxr, min, max, outliers);
    }
}
