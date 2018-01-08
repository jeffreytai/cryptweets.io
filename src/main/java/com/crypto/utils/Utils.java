package com.crypto.utils;

import java.math.BigDecimal;
import java.util.Date;

public class Utils {

    /**
     * Replace all non-numeric characters except for decimals in a string
     * @param value
     * @return
     */
    public static BigDecimal sanitizeDecimalString(String value) {
        try {
            String sanitizedValue = value.replaceAll("[^\\d.]", "");
            return new BigDecimal(sanitizedValue);
        } catch (NumberFormatException ex) {
            return null;
        }

    }

    /**
     * Convert Unix timestamp to datetime
     * @param unixTimestamp
     * @return
     */
    public static Date convertUnixToDateTime(Long unixTimestamp) {
        Date date = new Date(unixTimestamp * 1000);
        return date;
    }

    /**
     * Convert datetime to Unix timestamp
     * @param datetime
     * @return
     */
    public static Long convertDatetimeToUnix(Date datetime) {
        return datetime.getTime();
    }
}
