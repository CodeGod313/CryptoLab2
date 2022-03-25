package com.cleverdeath.cryptolabsecond.reader;

import java.nio.file.Path;
import java.util.Optional;

public interface FileProcessor {
    Optional<String> readTextFromFile(Path path);
    void writeToFile(String fileName, String text);
}
