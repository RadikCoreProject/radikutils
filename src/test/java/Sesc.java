import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.radikutils.drawing.BarDrawing;
import org.radikutils.drawing.Dataset;
import org.radikutils.drawing.TimeSeriesDrawing;
import org.radikutils.parser.CSVParser;
import org.radikutils.parser.ConditionOperator;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Sesc {
    public static void main(String[] args) {
        File file = new File("/home/radik/Загрузки/dataset_file_storage.csv");
        CSVParser parser = new CSVParser(file, ";");
        n0(parser);
//        sleep(2000);
//        n1(parser);
//        sleep(2000);
//        n2(parser);
//        sleep(2000);
//        n3(parser);
        n4(parser);
//        n5(parser);
    }

    private static void n0(CSVParser parser) {
        parser.parseList();
        parser.parseMap();
        parser.sort("uploadServerUnixTime");
        parser.modify((headers, val) -> {
            String t = val[2];
            DateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return new String[]{val[0], val[1], dt.format(new Date(Long.parseLong(t) * 1000)), val[3], val[4], val[5]};
        });
        System.out.println("Дата модифицирована TimeStamp -> dd/MM/yyyy HH:mm:ss");
        sleep(3000);
        parser.remove(ConditionOperator.OR, (headers, val) -> val[2].contains("/03/"));
        System.out.println("Удалён март месяц");
    }

    private static void n1(CSVParser parser) {
        Dataset<CSVParser, XYSeriesCollection> dataset = CSVParser.genTimeDataset("uploadServerUnixTime", "FileSize", (s, c) -> {
            String[] t = s.split("/");
            return t[1] + "/" + t[2].split(" ")[0];
        });
        TimeSeriesDrawing<CSVParser, XYSeriesCollection> drawer = new TimeSeriesDrawing<>("Задание 1.1", "Дата", "Вес файлов", parser, dataset, true);
        drawer.draw();
        System.out.println("График #1 нарисован успешно");
    }

    private static void n2(CSVParser parser) {
        System.out.println("Прогноз на 2020 год: S = (8192058931 + 7022650581 + 3391116262) / 3 = 6201941925 Байт / месяц");
        System.out.println("826412626 + 9018471557 + 16041122138 + 19432238400 + 25613154112 + S * 10 = 87632573362 Байт = 81,65ГБ");
    }

    private static void n3(CSVParser parser) {
        DefaultCategoryDataset set = new DefaultCategoryDataset();
        String[] months = { "10/2019", "11/2019", "12/2019", "01/2020", "02/2020", "03/2020", "04/2020", "05/2020", "06/2020", "07/2020", "08/2020", "09/2020", "10/2020", "11/2020", "12/2020" };
        double[] values = { 826412626L, 9018471557L, 16041122138L, 19432238400L, 25613154112L, 31815096037L, 38017037962L, 44218979887L, 50420921812L, 56622863737L, 62824805662L, 69026747587L, 75228689512L, 81430631437L, 87632573362L };
        for (int i = 0; i < months.length; i++) {
            set.addValue(values[i], "Накопленный объём", months[i]);
        }
        Dataset<CSVParser, DefaultCategoryDataset> dataset = parser1 -> set;
        BarDrawing<CSVParser, DefaultCategoryDataset> drawer1 = new BarDrawing<>(
            "Задание 1.3",
            "Месяц",
            "Объём",
            parser,
            dataset,
            true
        );
        drawer1.draw();
        System.out.println("График #2 нарисован успешно");
    }

    private static void n4(CSVParser parser) {
        Dataset<CSVParser, DefaultCategoryDataset> dataset = CSVParser.genDataset("uploadServerUnixTime", "FileSize", "", val -> {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = sdf.parse(val);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                int europeanDay = (dayOfWeek == Calendar.SUNDAY) ? 7 : dayOfWeek - 1;
                String[] names = {"понедельник", "вторник", "среда", "четверг", "пятница", "суббота", "воскресенье"};
                return europeanDay + ". " + names[europeanDay - 1];
            } catch (ParseException e) {
                return "0. Неизвестно";
            }
        });
        BarDrawing<CSVParser, DefaultCategoryDataset> drawer = new BarDrawing<>("Задание 2", "День недели", "Количество файлов", parser, dataset, false);
        drawer.draw();
        System.out.println("График #3 нарисован успешно");
    }

    private static void n5(CSVParser parser) {
        Dataset<CSVParser, DefaultCategoryDataset> dataset = CSVParser.genDataset("uploadServerUnixTime", "FileSize", "", val -> {
            String[] parts = val.split(" ");
            String timePart = parts[1];
            String hour = timePart.split(":")[0];
            return hour.length() == 1 ? "0" + hour : hour;
        });
        BarDrawing<CSVParser, DefaultCategoryDataset> drawer = new BarDrawing<>("Задание 2", "Часы", "Количество файлов", parser, dataset, false);
        drawer.draw();
        System.out.println("График #3 нарисован успешно");
    }

    private static void n6(CSVParser parser) {

    }

    private static void n7(CSVParser parser) {

    }


    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
