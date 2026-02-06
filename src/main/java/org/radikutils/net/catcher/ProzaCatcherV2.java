package org.radikutils.net.catcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ProzaCatcherV2 implements Catcher {
    public ProzaCatcherV2() throws IOException {
        base();
    }


    public String[] catching(String url) {
        String[] answer = new String[3];
        StringBuilder ans = new StringBuilder();
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36").timeout(10000).get();
            Elements texts = doc.select("z");
            ans.append("=== BASED TEXT ===\n");
            ans.append("Text fragments count: ").append(texts.size()).append("\n");

            StringBuilder finalAns1 = ans;
            texts.forEach(element -> {
                String text = element.text().trim().replace("\u0097", "-");

                if (!text.isEmpty()) {
                    finalAns1.append(String.format("\t%s\n", text));
                }
            });

            ans.append("=====METADATA=====").append("\n");

            Elements author = doc.select("div.author");
            if (!author.isEmpty()) ans.append("Author: ").append(Objects.requireNonNull(author.first()).text()).append("\n");

            Elements title = doc.select("div.title");
            if (!title.isEmpty()) ans.append("Title: ").append(Objects.requireNonNull(title.first()).text()).append("\n");

            Elements head = doc.select("h2, h3");
            if (head.size() == 2) {
                answer[1] = head.getFirst().text();
                answer[2] = head.get(1).text();
            } else {
                answer[2] = head.getFirst().text();
            }


        } catch (IOException e) {
            ans = new StringBuilder("Error: " + e.getMessage());
        }
        answer[0] = ans.toString();
        return answer;
    }

    private static String getTitle(String url) {
        String ans = "";
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36").timeout(10000).get();
            Elements author = doc.select("div.author");
            if (!author.isEmpty()) ans += String.format("%s |", Objects.requireNonNull(author.first()).text());

            ans += " ";

            Elements title = doc.select("div.title");
            if (!title.isEmpty()) ans += String.format("%s", Objects.requireNonNull(title.first()).text());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ans;
    }

    public void calc() throws IOException {
        File file = new File("files/Произведения.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (!file.exists()) {
                if (!file.createNewFile()) throw new IOException();
            }

            for (int i = 1; i < 999; i++) {
                if ((i + 1) % 50 == 0) System.out.print("⬛️ ");
                String p = getTitle(String.format("https://ilibrary.ru/text/%s/p.1/index.html", i));
                if (p.split(" \\| ")[1].equals("* * *")) continue;
                writer.write(String.format("%d | %s", i, p));
                writer.newLine();

            }
            System.out.println();
        }
    }

    private HashMap<String, HashMap<String, String>> getAuthors() throws IOException {
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

        }

        return answer;
    }

    private boolean getText(String id, String author, String proza) {
        String part = "";
        try {
            Path path = Path.of(String.format("files/%s/%s", author, proza));
            if (Files.exists(path)) {
                System.out.println("Это произведение уже загружено!");
                return false;
            }

            Files.createDirectories(path);
            for (int i = 1; i < 200; i++) {
                String[] catching = this.catching(String.format("https://ilibrary.ru/text/%s/p.%d/index.html", id, i));
                if (catching[1] != null) part = catching[1];
                String s = part.isEmpty() ? String.format("files/%s/%s/Глава %s.txt", author, proza, catching[2]) : String.format("files/%s/%s/%s, глава %s.txt", author, proza, part, catching[2]);
                File file = new File(s);
                if (!file.createNewFile()) throw new IOException();

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(catching[0]);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ignored) {}

        return true;
    }

    public void base() throws IOException {
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
                                            if (getText(proz.get(proza), author, proza)) System.out.println("скачивание завершено!");
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
}
