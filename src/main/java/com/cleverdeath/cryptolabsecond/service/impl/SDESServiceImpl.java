package com.cleverdeath.cryptolabsecond.service.impl;

import com.cleverdeath.cryptolabsecond.exception.SDESServiceException;
import com.cleverdeath.cryptolabsecond.service.SDESService;
import com.cleverdeath.cryptolabsecond.validator.KeyValidator;
import com.cleverdeath.cryptolabsecond.validator.impl.KeyValidatorImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SDESServiceImpl implements SDESService {

    public static final char CHAR_ZERO = '0';
    public static final char CHAR_ONE = '1';
    private static final Integer[] P10 = {2, 4, 1, 6, 3, 9, 0, 8, 7, 5};
    private static final Integer[] P8 = {5, 2, 6, 3, 7, 4, 9, 8};
    private static final Integer[] P4 = {1, 3, 2, 0};
    private static final Integer[] IP = {1, 5, 2, 0, 3, 7, 4, 6};
    private static final Integer[] IP_FINALE = {3, 0, 2, 4, 6, 1, 7, 5};
    private static final Integer[] EP = {3, 0, 1, 2, 1, 2, 3, 0};
    private static final Integer[][] S_BLOCK1 = {{1, 0, 3, 2}, {3, 2, 1, 0}, {0, 2, 1, 3}, {3, 1, 3, 2}};
    private static final Integer[][] S_BLOCK2 = {{0, 1, 2, 3}, {2, 0, 1, 3}, {3, 0, 1, 0}, {2, 1, 0, 3}};

    @Override
    public String encryptString(String text, String key) throws SDESServiceException {
        if (validateParameters(text, key)) {
            throw new SDESServiceException("Passed parameters are bad");
        }
        StringBuilder encryptedSequence = new StringBuilder();
        List<String> sequences = convertStringToCharSequences(text);
        List<String> keys = generateKeys(key);
        sequences.forEach(x -> {
            String processedSequence = processSDES(x, keys.get(0), keys.get(1));
            char encryptedSymbol = convertBinaryStringToChar(processedSequence);
            encryptedSequence.append(encryptedSymbol);
        });
        return encryptedSequence.toString();
    }

    @Override
    public String decryptString(String text, String key) throws SDESServiceException {
        if (validateParameters(text, key)) {
            throw new SDESServiceException("Passed parameters are bad");
        }
        StringBuilder decryptedSequence = new StringBuilder();
        List<String> sequences = convertStringToCharSequences(text);
        List<String> keys = generateKeys(key);
        sequences.forEach(x -> {
            String processedSequence = processSDES(x, keys.get(1), keys.get(0));
            char decryptedSymbol = convertBinaryStringToChar(processedSequence);
            decryptedSequence.append(decryptedSymbol);
        });
        return decryptedSequence.toString();
    }

    private boolean validateParameters(String text, String key) {
        KeyValidator keyValidator = new KeyValidatorImpl();
        return text == null || !keyValidator.validateKey(key);
    }

    private List<String> generateKeys(String key) {
        StringBuilder mixedKeyBuilder = new StringBuilder();
        Arrays.stream(P10).forEach(x -> mixedKeyBuilder.append(key.charAt(x)));
        StringBuilder leftBitsBuilder = new StringBuilder(mixedKeyBuilder.subSequence(0, 5));
        StringBuilder rightBitsBuilder = new StringBuilder(mixedKeyBuilder.subSequence(5, 10));
        cyclicLeftShift(leftBitsBuilder, 1);
        cyclicLeftShift(rightBitsBuilder, 1);
        String leftAndRightBitsForK1 = leftBitsBuilder + rightBitsBuilder.toString();
        StringBuilder k1 = new StringBuilder();
        Arrays.stream(P8).forEach(x -> k1.append(leftAndRightBitsForK1.charAt(x)));
        cyclicLeftShift(leftBitsBuilder, 2);
        cyclicLeftShift(rightBitsBuilder, 2);
        String leftAndRightBitsForK2 = leftBitsBuilder + rightBitsBuilder.toString();
        StringBuilder k2 = new StringBuilder();
        Arrays.stream(P8).forEach(x -> k2.append(leftAndRightBitsForK2.charAt(x)));
        List<String> keys = new ArrayList<>();
        keys.add(k1.toString());
        keys.add(k2.toString());
        return keys;
    }

    private void cyclicLeftShift(StringBuilder sequence, int number) {
        for (int i = 0; i < number; i++) {
            sequence.append(sequence.charAt(0));
            sequence.deleteCharAt(0);
        }
    }

    private String processRound(String sequence, String key) {
        String leftBits = sequence.substring(0, 4);
        String rightBits = sequence.substring(4);
        StringBuilder epBuilder = new StringBuilder();
        Arrays.stream(EP).forEach(x -> epBuilder.append(rightBits.charAt(x)));
        String epAfterXor = doXor(epBuilder.toString(), key);
        int firstIndexBlock1 = (epAfterXor.charAt(0) - CHAR_ZERO) * 2 + epAfterXor.charAt(3) - CHAR_ZERO;
        int secondIndexBlock1 = (epAfterXor.charAt(1) - CHAR_ZERO) * 2 + epAfterXor.charAt(2) - CHAR_ZERO;
        int firstIndexBlock2 = (epAfterXor.charAt(4) - CHAR_ZERO) * 2 + epAfterXor.charAt(7) - CHAR_ZERO;
        int secondIndexBlock2 = (epAfterXor.charAt(5) - CHAR_ZERO) * 2 + epAfterXor.charAt(6) - CHAR_ZERO;
        String leftBitsPair = Integer.toBinaryString(S_BLOCK1[firstIndexBlock1][secondIndexBlock1]);
        String rightBitsPair = Integer.toBinaryString(S_BLOCK2[firstIndexBlock2][secondIndexBlock2]);
        if (leftBitsPair.length() == 1) {
            leftBitsPair = '0' + leftBitsPair;
        }
        if (rightBitsPair.length() == 1) {
            rightBitsPair = '0' + rightBitsPair;
        }
        String bitsAfterBlocks = leftBitsPair + rightBitsPair;
        StringBuilder bitsAfterP4 = new StringBuilder();
        Arrays.stream(P4).forEach(x -> bitsAfterP4.append(bitsAfterBlocks.charAt(x)));
        String leftBitsOfAnswer = doXor(bitsAfterP4.toString(), leftBits);
        return leftBitsOfAnswer + rightBits;
    }

    private String doXor(String firstSequence, String secondSequence) {
        StringBuilder answerBuilder = new StringBuilder();
        for (int i = 0; i < firstSequence.length(); i++) {
            if ((firstSequence.charAt(i) == CHAR_ONE && secondSequence.charAt(i) == CHAR_ONE) ||
                    (firstSequence.charAt(i) == CHAR_ZERO && secondSequence.charAt(i) == CHAR_ZERO)) {
                answerBuilder.append(CHAR_ZERO);
            } else {
                answerBuilder.append(CHAR_ONE);
            }
        }
        return answerBuilder.toString();
    }

    private String processSDES(String sequence, String k1, String k2) {
        StringBuilder firstMix = new StringBuilder();
        Arrays.stream(IP).forEach(x -> firstMix.append(sequence.charAt(x)));
        String afterFirstRound = processRound(firstMix.toString(), k1);
        String leftBits = afterFirstRound.substring(0, 4);
        String rightBits = afterFirstRound.substring(4, 8);
        String afterSwap = rightBits + leftBits;
        String afterSecondRound = processRound(afterSwap, k2);
        StringBuilder secondMix = new StringBuilder();
        Arrays.stream(IP_FINALE).forEach(x -> secondMix.append(afterSecondRound.charAt(x)));
        return secondMix.toString();
    }

    private List<String> convertStringToCharSequences(String text) {
        List<String> sequences = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            String binarySequence = Integer.toBinaryString(text.charAt(i));
            sequences.add(completeSequenceWithZeros(binarySequence));
        }
        return sequences;
    }

    private String completeSequenceWithZeros(String sequence) {
        StringBuilder sequenceBuilder = new StringBuilder(sequence);
        while (sequenceBuilder.length() < 8) {
            sequenceBuilder.insert(0, CHAR_ZERO);
        }
        return sequenceBuilder.toString();
    }

    private char convertBinaryStringToChar(String binaryString) {
        int number = Integer.parseInt(binaryString, 2);
        return (char) number;
    }
}