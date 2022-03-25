package com.cleverdeath.cryptolabsecond.reader.impl;

import com.cleverdeath.cryptolabsecond.reader.FileProcessor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class FileProcessorImpl implements FileProcessor {
    @Override
    public Optional<String> readTextFromFile(Path path) {
        try {
            return Optional.of(new String(Files.readAllBytes(path)));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public void writeToFile(String fileName, String text) {
        PrintWriter writer;
        try {
            writer = new PrintWriter("EncryptedText.txt", StandardCharsets.UTF_8);
            writer.print(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
