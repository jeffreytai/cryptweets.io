package com.crypto.utils;

import java.math.BigDecimal;
import java.util.Date;

public class Utils {

    /**
     * Replace all non-numeric characters except for decimals in a string
     * @param value
     * @return BigDecimal datatype
     */
    public static BigDecimal sanitizeStringToBigDecimal(String value) {
        try {
            String sanitizedValue = value.replaceAll("[^\\d.]", "");
            return new BigDecimal(sanitizedValue);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    /**
     * Replace all non-numeric characters except for decimals in a string
     * @param value
     * @return double datatype
     */
    public static Double sanitizeStringToDouble(String value) {
        String sanitizedValue = value.replaceAll("[^\\d.]", "");
        return Double.parseDouble(sanitizedValue);
    }

    /**
     * Round a decimal to the nearest two decimal places
     * @param value
     * @return
     */
    public static Double roundDecimal(Double value) {
        Double rounded = (double) Math.round(value * 100) / 100;
        return rounded;
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
