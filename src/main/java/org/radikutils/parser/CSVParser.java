package org.radikutils.parser;

import org.jfree.data.category.DefaultCategoryDataset;
import org.radikutils.drawing.Dataset;

import java.io.*;
import java.util.*;

public class CSVParser implements Parser {
    public final File file;
    public final String spliterator;

    public HashMap<String, String[]> mass = new HashMap<>();

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

    public static Dataset<CSVParser> genDataset(String row, String column) {
        return genDataset(row, column, val -> 1);
    }

    public static Dataset<CSVParser> genDataset(String row, String column, DrawCondition condition) {
        return parser1 -> {
            boolean c = condition.type() != null;
            DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
            HashMap<String, String[]> mass = parser1.mass;

            String[] first = mass.get(column);
            String[] second = mass.get(row);
            String[] third = new String[first.length];

            if (c) third = mass.get(condition.type());
            HashMap<String, HashMap<String, Integer>> parse = new HashMap<>();
            for (int i = 0; i < first.length; i++) {
                String d = second[i];
                String t = first[i];
                String th = third[i];
                parse.computeIfAbsent(d, k -> new HashMap<>()).merge(t, condition.cond(th), Integer::sum);
            }

            parse.forEach((k, v) -> {
                if (!k.isEmpty()) {
                    v.forEach((k1, v1) -> dataset1.addValue(v1, k, k1));
                }
            });
            return dataset1;
        };
    }

    public static Dataset<CSVParser> genDataset(String row, String valueColumn, String n) {
        return parser -> {
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            HashMap<String, String[]> mass = parser.mass;

            String[] first = mass.get(row);
            String[] third = mass.get(valueColumn);
            Map<String, Double> sums = new HashMap<>();

            for (int i = 0; i < first.length; i++) {
                String cat = first[i];
                double val = Double.parseDouble(third[i]);
                sums.merge(cat, val, Double::sum);
            }

            sums.forEach((date, sum) -> dataset.addValue(sum, n, date));

            return dataset;
        };
    }

    public void remove(ConditionOperator operator, RemoveCondition... modifiers) {
        ArrayList<String[]> newData = removeData(operator, modifiers);
        HashMap<String, String[]> newMass = removeMass(operator, modifiers);
        data = newData;
        mass = newMass;
    }

    public HashMap<String, String[]> removeMass(ConditionOperator operator, RemoveCondition... modifiers) {
        HashMap<String, String[]> newMass = new HashMap<>();
        int rowCount = mass.values().iterator().next().length;
        int colCount = mass.size();
        List<String> keys = List.copyOf(mass.keySet());

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
        HashMap<String, String[]> newMass = modifyMass(modifier);
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

    private HashMap<String, String[]> modifyMass(ModifyCondition modifier) {
        if (mass.isEmpty()) return new HashMap<>();

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

        HashMap<String, String[]> newMass = new HashMap<>();
        for (int j = 0; j < c; j++) {
            String colName = keys.get(j);
            String[] colValues = new String[r];
            for (int i = 0; i < r; i++) colValues[i] = transformed[i][j];
            newMass.put(colName, colValues);
        }

        if (headers != null && !headers.isEmpty()) {
            headers.clear();
            headers.addAll(keys);
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

    @FunctionalInterface
    public interface DrawCondition {
        int cond(String val);
        default int sum(int a) {return a + 1;}
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
}
