package org.radikutils;

import org.radikutils.drawing.BarDrawing;
import org.radikutils.drawing.Dataset;
import org.radikutils.parser.CSVParser;

import java.io.File;

import static org.radikutils.parser.CSVParser.genDataset;

public class Main {
    public static void main(String[] args) {
        File file = new File("/home/radik/Загрузки/tips.csv");
        CSVParser parser = new CSVParser(file);
        parser.parse();
        File file2 = new File("/home/radik/Загрузки/titanic.csv");
        CSVParser parser2 = new CSVParser(file2);
        parser2.parse();
        Dataset<CSVParser> dataset = genDataset("day", "time");
        BarDrawing<CSVParser> drawer = new BarDrawing<>("Задание 1", "Дни недели", "Количество гостей", parser, dataset);
        drawer.draw();

        Dataset<CSVParser> dataset1 = genDataset("size", "sex");
        BarDrawing<CSVParser> drawer1 = new BarDrawing<>("Задание 2", "Количество мест за столом", "Количество гостей", parser, dataset1);
        drawer1.draw();

        Dataset<CSVParser> dataset2 = genDataset("pclass", "sex");
        BarDrawing<CSVParser> drawer2 = new BarDrawing<>("Задание 3", "Классы", "Количество людей", parser2, dataset2);
        drawer2.draw();

        Dataset<CSVParser> dataset3 = genDataset("deck", "sex");
        BarDrawing<CSVParser> drawer3 = new BarDrawing<>("Задание 4", "Палубы", "Количество людей", parser2, dataset3);
        drawer3.draw();

        Dataset<CSVParser> dataset4 = genDataset("deck", "pclass", new CSVParser.Condition() {
            @Override
            public int cond(String val) {
                return val.equals("1") ? 1 : 0;
            }

            @Override
            public String type() {
                return "survived";
            }
        });
        BarDrawing<CSVParser> drawer4 = new BarDrawing<>("Задание 5", "Палубы", "Количество людей", parser2, dataset4);
        drawer4.draw();
    }
}
