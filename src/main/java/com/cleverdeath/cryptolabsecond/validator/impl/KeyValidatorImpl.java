package com.cleverdeath.cryptolabsecond.validator.impl;

import com.cleverdeath.cryptolabsecond.validator.KeyValidator;

public class KeyValidatorImpl implements KeyValidator {

    public static final String REGEX_KEY = "[01]{10}";

    @Override
    public boolean validateKey(String key) {
        return key != null && key.matches(REGEX_KEY);
    }
}
