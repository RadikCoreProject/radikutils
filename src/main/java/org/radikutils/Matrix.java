package org.radikutils;

import org.jetbrains.annotations.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Matrix<T extends Number> {
    public Matrix(ArrayList<ArrayList<T>> matrix) {
        this.matrix = matrix;
    }

    @SuppressWarnings("unchecked")
    public Matrix(int rows, int cols) {
        matrix = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        ArrayList<T> l = new ArrayList<>();
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                l.add((T) (Double) sc.nextDouble());
            }
            matrix.add(l);
            l = new ArrayList<>();
        }
    }

    @SuppressWarnings("unchecked")
    public Matrix(@NotNull String str) {
        ArrayList<ArrayList<T>> ans = new ArrayList<>();
        ArrayList<T> row = new ArrayList<>();
        for(String s : str.split("\n")) {
            for(String t : s.split(" ")) {
                row.add((T) Integer.valueOf(t));
            }
            ans.add(row);
            row = new ArrayList<>();
        }
        this.matrix = ans;
    }

    public ArrayList<ArrayList<T>> matrix;

    public ArrayList<ArrayList<T>> getMatrix() {
        return matrix;
    }

    public void setMatrix(ArrayList<ArrayList<T>> matrix) {
        this.matrix = matrix;
    }

    public T get(int row, int col) {
        return matrix.get(row).get(col);
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder();
        for (ArrayList<T> ts : matrix) {
            ans.append("[");
            for (T t : ts) {
                ans.append(t).append(" ");
            }
            ans = new StringBuilder(ans.toString().strip() + "]");
            ans.append("\n");
        }
        return ans.toString().strip();
    }

    // костыль, но работает - не трогать
    @SuppressWarnings("unchecked")
    public void addMatrix(Matrix<T> sumMatrix) {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                matrix.get(i).set(j, (T) Integer.valueOf(sumMatrix.get(i, j).intValue() + matrix.get(i).get(j).intValue()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void substractMatrix(Matrix<T> sumMatrix) {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                matrix.get(i).set(j, (T) Integer.valueOf(matrix.get(i).get(j).intValue() - sumMatrix.get(i, j).intValue()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void addNumber(T num) {
        for (ArrayList<T> ts : matrix) {
            ts.replaceAll(t -> (T) Integer.valueOf(t.intValue() + num.intValue()));
        }
    }

    @SuppressWarnings("unchecked")
    public void substractNumber(T num) {
        for (ArrayList<T> ts : matrix) {
            ts.replaceAll(t -> (T) Integer.valueOf(t.intValue() - num.intValue()));
        }
    }

    // TODO: не работает еще
    @SuppressWarnings("unchecked")
    public void multiplyMatrix(Matrix<T> sumMatrix) {
        for (ArrayList<T> ts : matrix) {
            for (int j = 0; j < ts.size(); j++) {

            }
        }
    }
}
