package org.radikutils.net.catcher;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class CatchText {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("files");
        Files.createDirectories(path);
        try {
            new ProzaCatcherV2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
