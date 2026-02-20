package org.radikutils.parser.csv;

import org.radikutils.parser.ConditionOperator;
import org.radikutils.parser.ParseType;
import org.radikutils.parser.Parser;
import org.radikutils.plets.Duplet;

import java.io.*;
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
