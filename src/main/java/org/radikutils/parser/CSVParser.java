package org.radikutils.parser;

import org.jfree.data.category.DefaultCategoryDataset;
import org.radikutils.drawing.Dataset;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CSVParser implements Parser {
    public final File file;
    public HashMap<String, String[]> mass = new HashMap<>();

    public CSVParser(File file) {
        this.file = file;
    }

    public boolean parse() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<String> lines = br.lines().toList();
            if (lines.isEmpty()) return false;
            String[] fl = lines.getFirst().replace("\"", "").split(",");
            int size = lines.size() - 1;
            Arrays.stream(fl).forEach(s -> mass.put(s, new String[size]));

            for (int i = 1; i < lines.size(); i++) {
                String[] s = lines.get(i).split(",");
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

    public void print() {
        mass.forEach((k, v) -> {
            System.out.print(k + ": ");
            System.out.println(Arrays.toString(v));
        });
    }

    public static Dataset<CSVParser> genDataset(String row, String column) {
        return genDataset(row, column, val -> 1);
    }

    public static Dataset<CSVParser> genDataset(String row, String column, Condition condition) {
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

    @FunctionalInterface
    public interface Condition {
        int cond(String val);
        default String type() {return null;}
    }
}
