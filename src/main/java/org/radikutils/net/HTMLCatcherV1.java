package org.radikutils.net;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.radikutils.plets.Triplet;

import java.io.IOException;

public class HTMLCatcherV1 {
    public static String[] catching(String url) {
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
            if (!author.isEmpty()) ans.append("Author: ").append(author.first().text()).append("\n");

            Elements title = doc.select("div.title");
            if (!title.isEmpty()) ans.append("Title: ").append(title.first().text()).append("\n");

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
}
