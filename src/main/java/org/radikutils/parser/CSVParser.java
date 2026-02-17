package org.radikutils.parser;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.radikutils.drawing.Dataset;
import org.radikutils.plets.Duplet;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVParser implements Parser {
    public final File file;
    public final String spliterator;

    public Map<String, String[]> mass = new LinkedHashMap<>();

    public List<String> headers = new ArrayList<>();
    public ArrayList<String[]> data = new ArrayList<>();

    public CSVParser(File file, String spliterator) {
        this.file = file;
        this.spliterator = spliterator;
    }

    public boolean parseMap() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<String> lines = br.lines().toList();
            if (lines.isEmpty()) return false;
            String[] fl = lines.getFirst().replace("\"", "").split(spliterator);
            int size = lines.size() - 1;
            Arrays.stream(fl).forEach(s -> mass.put(s, new String[size]));

            for (int i = 1; i < lines.size(); i++) {
                String[] s = lines.get(i).split(spliterator);
                for (int j = 0; j < s.length; j++) {
                    String[] p = mass.get(fl[j]);
                    p[i - 1] = s[j].replace("\"", "");
                    mass.put(fl[j], p);
                }
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean parseList() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<String> lines = br.lines().toList();
            if (lines.isEmpty()) return false;
            headers =  Arrays.stream(lines.getFirst().replace("\"", "").split(spliterator)).toList();
            for (int i = 1; i < lines.size(); i++) {
                String[] s = lines.get(i).split(spliterator);
                data.add(s);
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void print(boolean list) {
        if (list) {
            headers.forEach(t -> System.out.print(t + "; "));
            System.out.println();
            for (String[] s : data) {
                System.out.println(Arrays.toString(s));
            }
        } else {
            mass.forEach((k, v) -> {
                System.out.print(k + ": ");
                System.out.println(Arrays.toString(v));
            });
        }
    }

    public void sort(String column) {
        int colIndex = headers.indexOf(column);
        if (colIndex == -1) throw new IllegalArgumentException("Column not found: " + column);
        if (data.isEmpty() || mass.isEmpty()) return;

        List<Duplet<String, Integer>> pairs = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            String value = row[colIndex];
            pairs.add(new Duplet<>(value, i));
        }

        pairs.sort(Comparator.comparing(Duplet::getType));
        ArrayList<String[]> newData = new ArrayList<>();
        for (Duplet<String, Integer> p : pairs) {
            newData.add(data.get(p.getParametrize()));
        }

        Map<String, String[]> newMass = new LinkedHashMap<>();
        for (Map.Entry<String, String[]> entry : mass.entrySet()) {
            String colName = entry.getKey();
            String[] oldCol = entry.getValue();
            String[] newCol = new String[oldCol.length];
            for (int newIdx = 0; newIdx < pairs.size(); newIdx++) {
                int oldIdx = pairs.get(newIdx).getParametrize();
                newCol[newIdx] = oldCol[oldIdx];
            }
            newMass.put(colName, newCol);
        }

        data = newData;
        mass = newMass;
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column) {
        return genDataset(row, column, val -> 1);
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column, DrawCondition condition) {
        return genDataset(row, column, condition, null, null);
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column, String condition, CountingCondition condition1) {
        return genDataset(row, column, val -> 1, t -> condition, condition1);
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column, CountingCondition condition, String condition1) {
        return genDataset(row, column, val -> 1, condition, t -> condition1);
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column, CountingCondition condition, CountingCondition condition1) {
        return genDataset(row, column, val -> 1, condition, condition1);
    }

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column, DrawCondition condition, CountingCondition condition1, CountingCondition condition2) {
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

    public static Dataset<CSVParser, DefaultCategoryDataset> genDataset(String row, String column, DisplayCondition rowCond, DisplayCondition columnCond) {
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

    public void remove(ConditionOperator operator, RemoveCondition... modifiers) {
        ArrayList<String[]> newData = removeData(operator, modifiers);
        Map<String, String[]> newMass = removeMass(operator, modifiers);
        data = newData;
        mass = newMass;
    }

    public Map<String, String[]> removeMass(ConditionOperator operator, RemoveCondition... modifiers) {
        if (mass.isEmpty()) return new LinkedHashMap<>();

        List<String> keys = (headers != null && !headers.isEmpty())
            ? new ArrayList<>(headers)
            : new ArrayList<>(mass.keySet());

        int rowCount = mass.get(keys.getFirst()).length;
        int colCount = keys.size();

        boolean[] keep = new boolean[rowCount];
        for (int i = 0; i < rowCount; i++) {
            String[] row = new String[colCount];
            for (int j = 0; j < colCount; j++) {
                row[j] = mass.get(keys.get(j))[i];
            }
            keep[i] = needRemove(operator, row, modifiers);
        }

        int keeps = 0;
        for (boolean b : keep) if (b) keeps++;

        Map<String, String[]> newMass = new LinkedHashMap<>();
        int finalKeep = keeps;
        keys.forEach(colName -> {
            String[] newCol = new String[finalKeep];
            String[] oldCol = mass.get(colName);
            int idx = 0;
            for (int i = 0; i < oldCol.length; i++) {
                if (keep[i]) newCol[idx++] = oldCol[i];
            }
            newMass.put(colName, newCol);
        });

        return newMass;
    }

    private ArrayList<String[]> removeData(ConditionOperator operator, RemoveCondition... modifiers) {
        ArrayList<String[]> newData = new ArrayList<>();
        for (String[] row : data) {
            if (needRemove(operator, row, modifiers)) newData.add(row);
        }
        return newData;
    }

    private boolean needRemove(ConditionOperator operator, String[] row, RemoveCondition... modifiers) {
        boolean match;
        if (operator == ConditionOperator.AND) {
            match = true;
            for (RemoveCondition cond : modifiers) {
                if (!cond.cond(headers, row)) {
                    match = false;
                    break;
                }
            }
        } else {
            match = false;
            for (RemoveCondition cond : modifiers) {
                if (cond.cond(headers, row)) {
                    match = true;
                    break;
                }
            }
        }
        return !match;
    }

    public void modify(ModifyCondition modifier) {
        ArrayList<String[]> newData = modifyData(modifier);
        Map<String, String[]> newMass = modifyMass(modifier);
        data = newData;
        mass = newMass;
    }

    private ArrayList<String[]> modifyData(ModifyCondition modifier) {
        ArrayList<String[]> newData = new ArrayList<>();
        for (String[] row : data) {
            String[] r = modifier.cond(headers, row);
            newData.add(r);
        }
        return newData;
    }

    private Map<String, String[]> modifyMass(ModifyCondition modifier) {
        if (mass.isEmpty()) return new LinkedHashMap<>();

        List<String> keys = (headers != null && !headers.isEmpty())
            ? new ArrayList<>(headers)
            : new ArrayList<>(mass.keySet());

        int r = mass.get(keys.getFirst()).length;
        int c = keys.size();

        String[][] transformed = new String[r][c];

        for (int i = 0; i < r; i++) {
            String[] row = new String[c];
            for (int j = 0; j < c; j++) row[j] = mass.get(keys.get(j))[i];
            transformed[i] = modifier.cond(keys, row);
        }

        Map<String, String[]> newMass = new LinkedHashMap<>();
        for (int j = 0; j < c; j++) {
            String colName = keys.get(j);
            String[] colValues = new String[r];
            for (int i = 0; i < r; i++) colValues[i] = transformed[i][j];
            newMass.put(colName, colValues);
        }

        return newMass;
    }

    public void save(ParseType type) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            if (type == ParseType.LIST) {
                bw.write(String.join(spliterator, headers));
                bw.newLine();
                for (String[] dat : data) {
                    bw.write(String.join(spliterator, dat));
                    bw.newLine();
                }
            } else {
                // TODO: map
            }
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static Dataset<CSVParser, XYSeriesCollection> genTimeDataset(String dateColumn, String valueColumn, DisplayCondition condition) {
        return genTimeDataset(dateColumn, valueColumn, s -> s, condition);
    }

    public static Dataset<CSVParser, XYSeriesCollection> genTimeDataset(String dateColumn, String valueColumn, CountingCondition cou, DisplayCondition condition) {
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

    @FunctionalInterface
    public interface DrawCondition {
        int cond(String val);
        default String type() {return null;}
    }

    @FunctionalInterface
    public interface RemoveCondition {
        boolean cond(List<String> headers, String[] val);
    }

    @FunctionalInterface
    public interface ModifyCondition {
        String[] cond(List<String> headers, String[] val);
    }

    @FunctionalInterface
    public interface DisplayCondition {
        String cond(String name, double sum);
    }

    @FunctionalInterface
    public interface CountingCondition {
        String cond(String name);
    }
}
