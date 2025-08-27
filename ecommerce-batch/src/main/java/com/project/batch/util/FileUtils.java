package com.project.batch.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileUtils {

    public static List<File> splitCsv(File csvFile, long fileCount) {
        long lineCount;
        try (Stream<String> stream = Files.lines(csvFile.toPath(), StandardCharsets.UTF_8)) {
            lineCount = stream.count();
            long linesPerFile = (long) Math.ceil((double) lineCount / fileCount);

            return splitFiles(csvFile, linesPerFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<File> splitFiles(File csvFile, long linesPerFile) throws IOException {
        List<File> splitFiles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            boolean firstLine = true;
            BufferedWriter writer = null;
            int lineCount = 0;
            boolean shouldCreateFile = true;
            File splitFile;
            int fileIndex = 0;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                if (shouldCreateFile) {
                    splitFile = createTempFile("split_" + (fileIndex++) + "_", ".csv");
                    writer = new BufferedWriter(new FileWriter(splitFile));
                    splitFiles.add(splitFile);
                    lineCount = 0;
                    shouldCreateFile = false;
                }

                writer.write(line);
                writer.newLine();
                lineCount++;

                if (lineCount >= linesPerFile) {
                    writer.close();
                    shouldCreateFile = true;
                }
            }
            writer.close();
        }
        return splitFiles;
    }

    public static File createTempFile(String prefix, String suffix) throws IOException {
        File tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit();
        return tempFile;
    }

}
