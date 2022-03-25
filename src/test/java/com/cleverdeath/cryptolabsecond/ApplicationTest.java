package com.cleverdeath.cryptolabsecond;

import com.cleverdeath.cryptolabsecond.exception.SDESServiceException;
import com.cleverdeath.cryptolabsecond.reader.FileProcessor;
import com.cleverdeath.cryptolabsecond.reader.impl.FileProcessorImpl;
import com.cleverdeath.cryptolabsecond.service.SDESService;
import com.cleverdeath.cryptolabsecond.service.impl.SDESServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Path;
import java.nio.file.Paths;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationTest {
    public static final String INPUT_FILE = "src/test/resources/inputTest.txt";
    public static final String OUTPUT_FILE = "src/test/resources/outputTest.txt";
    SDESService sdesService;
    FileProcessor fileProcessor;
    String key;

    @BeforeAll
    void setUp() {
        sdesService = new SDESServiceImpl();
        fileProcessor = new FileProcessorImpl();
        key = "1001010011";
    }

    @Test
    void generalTest() throws SDESServiceException {
        Path inputPath = Paths.get(INPUT_FILE);
        String text = fileProcessor.readTextFromFile(inputPath).get();
        String encryptedText = sdesService.encryptString(text, key);
        fileProcessor.writeToFile(OUTPUT_FILE, encryptedText);

        Path outputPath = Paths.get(OUTPUT_FILE);
        String encryptedTextFromFile = fileProcessor.readTextFromFile(outputPath).get();
        String decryptedText = sdesService.decryptString(encryptedTextFromFile, key);

        Assertions.assertEquals(text, decryptedText);
    }
}
