package org.radikutils.net;

import org.radikutils.plets.Duplet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CatchText {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("files");
        Files.createDirectories(path);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Выбери действие:\n1 - Вычислить все произведения\n2 - скачать произведение\n>> ");
            String s = sc.nextLine();

            switch (s) {
                case "1" -> {
                    System.out.println("Вычисляем все произведения...\nЭто может занять некоторое время");
                    calc();
                    System.out.println("Все произведения вычислены!\n\n");
                }
                case "2" -> {
                    System.out.println("Загрузка...");
                    HashMap<String, HashMap<String, String>> authors = getAuthors();
                    List<String> allAuthors = authors.keySet().stream().toList();
                    System.out.println("Все загруженные авторы:");
                    authors.keySet().forEach(System.out::println);

                    label_1: {
                        while (true) {
                            System.out.print("Введи фамилию автора. Для выхода введи 0:\n>> ");
                            String author = sc.nextLine();
                            if (author.equals("0")) break label_1;
                            else if (!allAuthors.contains(author)) System.out.println("Такой фамилии не нашлось :(");
                            else {
                                HashMap<String, String> proz = authors.get(author);
                                System.out.println("Все произведения " + author + ":");
                                proz.keySet().forEach(System.out::println);

                                label_2: {
                                    while (true) {
                                        System.out.print("Введи произведение автора. Для выхода введи 0:\n>> ");
                                        String proza = sc.nextLine();
                                        if (proza.equals("0")) break label_2;
                                        else if (!proz.containsKey(proza)) System.out.println("Такого произведения не нашлось :(");
                                        else {
                                            System.out.println("Выполняется скачивание...");
                                            getText(proz.get(proza));
                                            System.out.println("скачивание завершено!");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                default -> System.out.println("Неправильный ввод! попробуй еще раз");
            }
        }
    }

    private static void calc() {
        File file = new File("files/Произведения.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (!file.exists()) {
                if (!file.createNewFile()) throw new IOException();
            }

            for (int i = 1; i < 999; i++) {
                if ((i + 1) % 100 == 0) System.out.print("⬛️ ");
                try {
                    String p = HTMLCatcherV1.getName(String.format("https://ilibrary.ru/text/%s/p.1/index.html", i));
                    if (p.split(" \\| ")[1].equals("* * *")) continue;
                    writer.write(String.format("%d | %s", i, p));
                    writer.newLine();
                } catch (Exception ignored) {}

            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, HashMap<String, String>> getAuthors() {
        HashMap<String, HashMap<String, String>> answer = new HashMap<>();
        File file = new File("files/Произведения.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            if (!file.exists()) {
                if (!file.createNewFile()) throw new IOException();
            }

            for (String line : reader.lines().toList()) {
                String[] s = line.split(" \\| ");
                if (answer.containsKey(s[1])) {
                    HashMap<String, String> map = answer.get(s[1]);
                    map.put(s[2], s[0]);
                    answer.put(s[1], map);
                } else {
                    HashMap<String, String> helper = new HashMap<>();
                    helper.put(s[2], s[0]);
                    answer.put(s[1], helper);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer;
    }

    private static void getText(String id) {
        String part = "";
        try {
            for (int i = 1; i < 200; i++) {
                String[] catching = HTMLCatcherV1.catching(String.format("https://ilibrary.ru/text/%s/p.%d/index.html", id, i));
                if (catching[1] != null) part = catching[1];
                String s = part.isEmpty() ? String.format("files/Глава %s.txt", catching[2]) : String.format("files/%s, глава %s.txt", part, catching[2]);
                File file = new File(s);

                if (!file.exists()) {
                    if (!file.createNewFile()) throw new IOException();
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(catching[0]);
                    writer.newLine();
                }
            }
        } catch (Exception ignored) {}
    }
}
