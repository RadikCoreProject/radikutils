import org.radikutils.drawing.BarDrawing;
import org.radikutils.drawing.Dataset;
import org.radikutils.parser.CSVParser;
import org.radikutils.parser.ConditionOperator;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Sesc {
    public static void main(String[] args) {
        File file = new File("/home/radik/Загрузки/dataset_file_storage.csv");
        CSVParser parser = new CSVParser(file, ";");
        parser.parseList();
        parser.parseMap();
        parser.remove(ConditionOperator.OR, (headers, val) -> val[2].contains("/03/"));
        System.out.println("Удалён март месяц");
        parser.modify((headers, val) -> {
            String t = val[2];
            DateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            return new String[]{val[0], val[1], dt.format(new Date(Long.parseLong(t) * 1000)), val[3], val[4], val[5]};
        });
        System.out.println("Дата модифицирована UNIX -> dd/MM/yyyy");
        Dataset<CSVParser> dataset = CSVParser.genDataset("uploadServerUnixTime", "FileSize", "Дни");
        BarDrawing<CSVParser> drawer = new BarDrawing<>("Задание 1", "Дни", "Вес файлов", parser, dataset, true);
        drawer.draw();


//        parser.print(true);
    }
}
