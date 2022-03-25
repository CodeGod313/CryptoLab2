package com.cleverdeath.cryptolabsecond.service;

import com.cleverdeath.cryptolabsecond.exception.SDESServiceException;

public interface SDESService {
    String encryptString(String text, String key) throws SDESServiceException;
    String decryptString(String text, String key) throws SDESServiceException;
}
