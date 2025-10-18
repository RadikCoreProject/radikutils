package org.radikutils.net;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CatchText {
    public static void main(String[] args) {
        String part = "";
        for (int i = 1; i < 60; i++) {
            String[] catching = HTMLCatcherV1.catching("https://ilibrary.ru/text/96/p." + i + "/index.html");
            if (catching[1] != null) part = catching[1];
            String s = part.isEmpty() ? String.format("files/Глава %s.txt", catching[2]) : String.format("files/%s, глава %s.txt", part, catching[2]);
            File file = new File(s);

            try {
                if (!file.exists()) {
                    if (!file.createNewFile()) throw new IOException();
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write(catching[0]);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
