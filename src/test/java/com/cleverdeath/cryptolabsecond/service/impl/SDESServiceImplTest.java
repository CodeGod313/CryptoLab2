package com.cleverdeath.cryptolabsecond.service.impl;

import com.cleverdeath.cryptolabsecond.exception.SDESServiceException;
import com.cleverdeath.cryptolabsecond.service.SDESService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SDESServiceImplTest {

    SDESService sdesService;
    String message;
    String key;

    @BeforeAll
    void setUp() {
        sdesService = new SDESServiceImpl();
        message = "I LOVE YOU";
        key = "1001010011";
    }

    @Test
    void encryptString() throws SDESServiceException {
        String expected = "Y\u0018¾å\u0015¦\u0018`åg";
        String actual = sdesService.encryptString(message, key);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void decryptString() throws SDESServiceException {
        String expected = message;
        String actual = sdesService.decryptString("Y\u0018¾å\u0015¦\u0018`åg", key);
        Assertions.assertEquals(expected, actual);
    }
}