package com.crypto.sentiment;

import com.crypto.entity.CoinSentiment;
import com.crypto.exception.NoResultsFoundException;
import com.crypto.utils.ApiUtils;
import com.crypto.utils.DbUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.persistence.Query;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class SolumeIO {

    /**
     * Name of properties configuration file
     */
    private final String PROPERTIES_FILE = "solumeio.properties";

    /**
     * Name of key that holds api-key
     */
    private final String KEY_NAME = "id-token";

    /**
     * Base url for API
     */
    private final String BASE_URL = "https://api.solume.io/api/coins";

    /**
     * Api key value
     */
    private String token;


    public SolumeIO() {
        Properties prop = new Properties();

        try {
            InputStream inputStream = SolumeIO.class.getClassLoader().getResourceAsStream(this.PROPERTIES_FILE);
            prop.load(inputStream);
            inputStream.close();

            this.token = prop.getProperty(this.KEY_NAME);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Pull sentiment analysis for all coins listed on Solume.io
     * @return
     */
    public List<CoinSentiment> loadAllSentimentAnalysis() {
        JsonObject sentimentEntries = ApiUtils.getJson(this.BASE_URL, "auth", this.token);

        List<CoinSentiment> entries = extractEntitiesFromJson(sentimentEntries);
        return entries;
    }

    /**
     * Pull sentiment analysis for specified coin listed on Solume.io
     * @param symbol
     * @return
     */
    public CoinSentiment loadSentimentAnalysis(String symbol) throws NoResultsFoundException {
        JsonObject sentimentEntry = ApiUtils.getJson(this.BASE_URL, "auth", this.token, "symbol", symbol);

        if (sentimentEntry.keySet().size() == 0) {
            throw new NoResultsFoundException("No sentiment analysis found for " + symbol);
        }

        List<CoinSentiment> entries = extractEntitiesFromJson(sentimentEntry);
        return entries.get(0);
    }

    /**
     * Aggregates coins from JsonObject that is returned from API call
     * @param json
     * @return
     */
    private List<CoinSentiment> extractEntitiesFromJson(JsonObject json) {
        List<CoinSentiment> coinSentiments = new ArrayList<>();

        Date currentDate = new Date();

        Integer previousBatchNum = findLastBatchNumber();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            JsonObject info = entry.getValue().getAsJsonObject();

            CoinSentiment sentiment = new CoinSentiment(
                    entry.getKey(),
                    new BigDecimal(info.get("twitter_change_24h").toString()),
                    Long.parseLong(info.get("twitter_volume_24h").toString()),
                    new BigDecimal(info.get("reddit_change_24h").toString()),
                    Long.parseLong(info.get("reddit_volume_24h").toString()),
                    new BigDecimal(info.get("change_24h").toString()),
                    Long.parseLong(info.get("volume_24h").toString()),
                    currentDate,
                    previousBatchNum + 1
            );

            coinSentiments.add(sentiment);
        }

        return coinSentiments;
    }

    /**
     * Grab all sentiment analyses and save them to the database
     */
    public void saveSentiments() {
        List<CoinSentiment> sentiments = loadAllSentimentAnalysis();

        DbUtils.saveEntities(sentiments);
    }

    /**
     * Returns the max batch number from the CoinSentiment table
     * @return
     */
    public Integer findLastBatchNumber() {
        Object maxBatchNum = DbUtils.runSingularResultQuery("select MAX(c.batchNum) from CoinSentiment c");

        return maxBatchNum == null ? 0 : (Integer) maxBatchNum;
    }
}
