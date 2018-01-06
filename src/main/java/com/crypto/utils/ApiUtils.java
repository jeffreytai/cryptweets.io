package com.crypto.utils;

import com.crypto.exception.InvalidStringListException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApiUtils {

    /**
     * User Agent for web requests
     */
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36";

    public static JsonObject getJson(String baseUrl, String... parameters) {
        Map<String, String> parameterMap = generateParameterMapping(parameters);
        String requestUrl = generateUrl(baseUrl, parameterMap);
        String responseBody = getResponseBody(requestUrl);
        JsonObject obj = new JsonParser().parse(responseBody).getAsJsonObject();

        return obj;
    }

    private static Map<String, String> generateParameterMapping(String... parameters) {
        Map<String, String> parameterMap = null;

        try {
            parameterMap = generateMapFromStringList(parameters);
        } catch (InvalidStringListException e) {
            e.printStackTrace();
        }

        return parameterMap;
    }

    private static Map<String, String> generateMapFromStringList(String... parameters) throws InvalidStringListException {
        if (parameters.length % 2 != 0) {
            throw new InvalidStringListException("Invalid number of parameters");
        }

        Map<String, String> parameterMap = new LinkedHashMap<>();

        for (int i=0; i<parameters.length; i+=2) {
            parameterMap.put(parameters[i], parameters[i + 1]);
        }

        return Collections.unmodifiableMap(parameterMap);
    }

    private static String generateUrl(String baseUrl, Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl).append("?");

        for (Map.Entry<String, String> pair : parameters.entrySet()) {
            sb.append(pair.getKey() + "=" + pair.getValue() + "&");
        }

        return sb.toString();
    }

    private static String getResponseBody(final String requestUrl) {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }

                reader.close();

                return response.toString();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
