package org.radikutils.net;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class HTMLCatcherV1 {
    public static void main(String[] args) {
        String url = "https://ilibrary.ru/text/475/p.1/index.html";

        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36").timeout(10000).get();
            Elements texts = doc.select("z");
            System.out.println("=== BASED TEXT ===");
            System.out.println("Text fragments count: " + texts.size());
            System.out.println();

            int pc = 1;
            for (Element textElement : texts) {
                String text = textElement.text().trim().replace("\u0097", "-");

                if (!text.isEmpty()) {
                    System.out.println(String.format("[%d] %s", pc, text));
                    pc++;
                }
            }

            System.out.println("=====METADATA=====");

            Elements author = doc.select("div.author");
            if (!author.isEmpty()) System.out.println("Author: " + author.first().text());

            Elements title = doc.select("div.title");
            if (!title.isEmpty()) System.out.println("Title: " + title.first().text());

            Elements head = doc.select("h2, h3");
            for (Element heading : head) System.out.println("Hover: " + heading.text());

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
