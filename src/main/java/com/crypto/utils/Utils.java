package com.crypto.utils;

import java.math.BigDecimal;

public class Utils {

    /**
     * Replace all non-numeric characters except for decimals in a string
     * @param value
     * @return
     */
    public static BigDecimal sanitizeDecimalString(String value) {
        String sanitizedValue = value.replaceAll("[^\\d.]", "");
        return new BigDecimal(sanitizedValue);
    }
}
