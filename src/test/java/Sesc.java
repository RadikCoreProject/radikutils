import org.radikutils.parser.csv.CSVParser;

import java.io.File;

public class Sesc {
    public static void main(String[] args) {
        File file = new File("/home/radik/Загрузки/dataset_file_storage.csv");
        CSVParser parser = new CSVParser(file, ";");
    }
}
