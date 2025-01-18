package ru.kpfu.itis304.chat.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CurrencyService {

    private static final String API_KEY = "bb2bf4d12f584870a2a5deb958e14da6";
    private static final String API_URL = "https://openexchangerates.org/api/latest.json?app_id=";
    private static final String BASE_CURRENCY = "RUB";

    public String getExchangeRate(String targetCurrency) {
        StringBuilder result = new StringBuilder();
        try {
            String urlString = API_URL + API_KEY + "&base=" + targetCurrency;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    result.append(inputLine);
                }
                in.close();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(result.toString());
                if (jsonResponse.has("rates")) {
                    JsonNode rates = jsonResponse.get("rates");
                    if (rates.has(BASE_CURRENCY)) {
                        double convertedAmount = rates.get(BASE_CURRENCY).asDouble();
                        return String.format("1 RUB = %.2f %s", convertedAmount, targetCurrency
                        );
                    } else {
                        return "error: target currency not found.";
                    }
                } else {
                    return "error: unable to get exchange rates.";
                }
            } else {
                return "error: unable to get exchange rate data.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "error: " + e.getMessage();
        }
    }
}