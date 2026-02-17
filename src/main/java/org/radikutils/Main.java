package org.radikutils;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.radikutils.drawing.BarDrawing;
import org.radikutils.drawing.Dataset;
import org.radikutils.parser.CSVParser;

import java.io.File;
import java.net.URISyntaxException;

import static org.radikutils.parser.CSVParser.genDataset;

public class Main {
    public static void main(String[] args) throws URISyntaxException {
        String jarDir = new File(Main.class.getProtectionDomain()
            .getCodeSource()
            .getLocation()
            .toURI())
            .getParent();

        File file = new File(jarDir, "tips.csv");
        System.out.println(file.getAbsoluteFile());
        CSVParser parser = new CSVParser(file, ",");
        parser.parseMap();
        File file2 = new File(jarDir, "titanic.csv");
        System.out.println(file2.getAbsoluteFile());
        CSVParser parser2 = new CSVParser(file2, ",");
        parser2.parseMap();
        Dataset<CSVParser, DefaultCategoryDataset> dataset = genDataset("day", "time");
        BarDrawing<CSVParser, DefaultCategoryDataset> drawer = new BarDrawing<>("Задание 1", "Дни недели", "Количество гостей", parser, dataset, false);
        drawer.draw();

        Dataset<CSVParser, DefaultCategoryDataset> dataset1 = genDataset("size", "sex");
        BarDrawing<CSVParser, DefaultCategoryDataset> drawer1 = new BarDrawing<>("Задание 2", "Количество мест за столом", "Количество гостей", parser, dataset1, false);
        drawer1.draw();

        Dataset<CSVParser, DefaultCategoryDataset> dataset2 = genDataset("pclass", "sex");
        BarDrawing<CSVParser, DefaultCategoryDataset> drawer2 = new BarDrawing<>("Задание 3", "Классы", "Количество людей", parser2, dataset2, false);
        drawer2.draw();

        Dataset<CSVParser, DefaultCategoryDataset> dataset3 = genDataset("deck", "sex");
        BarDrawing<CSVParser, DefaultCategoryDataset> drawer3 = new BarDrawing<>("Задание 4", "Палубы", "Количество людей", parser2, dataset3, false);
        drawer3.draw();

        Dataset<CSVParser, DefaultCategoryDataset> dataset4 = genDataset("deck", "pclass", new CSVParser.DrawCondition() {
            @Override
            public int cond(String val) {
                return val.equals("1") ? 1 : 0;
            }

            @Override
            public String type() {
                return "survived";
            }
        });
        BarDrawing<CSVParser, DefaultCategoryDataset> drawer4 = new BarDrawing<>("Задание 5", "Палубы", "Количество людей", parser2, dataset4, false);
        drawer4.draw();
    }
}
