package com.crypto.sentiment;

import com.crypto.Constants;
import com.crypto.orm.entity.CoinSentiment;
import com.crypto.exception.NoResultsFoundException;
import com.crypto.orm.repository.SentimentRepository;
import com.crypto.slack.SlackWebhook;
import com.crypto.utils.ApiUtils;
import com.crypto.utils.DbUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

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

    /**
     * Base url for website
     */
    private final String WEBSITE_URL = "https://solume.io/";

    /**
     * Slack username to post as
     */
    private final String SLACK_ALERT_USERNAME = "social-volume-alert";


    public SolumeIO() {
        Properties prop = new Properties();

        try {
            InputStream inputStream = ResourceLoader.class.getClassLoader().getResourceAsStream(this.PROPERTIES_FILE);
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
    public List<CoinSentiment> loadAllSentimentAnalysis(Integer previousBatchNum) {
        List<CoinSentiment> entries = loadSentimentFromApi(previousBatchNum);

        return entries;
    }

    /**
     * Pull sentiment analysis for a specific coin listed on Solume.io
     * @param symbol
     * @return
     */
    public CoinSentiment loadSentimentAnalysis(String symbol, Integer previousBatchNum) throws NoResultsFoundException {
        JsonObject sentimentEntry = ApiUtils.getJson(this.BASE_URL, "auth", this.token, "symbol", symbol);

        if (sentimentEntry.keySet().size() == 0) {
            throw new NoResultsFoundException("No sentiment analysis found for " + symbol);
        }

        List<CoinSentiment> entries = extractEntitiesFromJson(sentimentEntry, previousBatchNum);
        return entries.get(0);
    }

    /**
     * Creates the list of CoinSentiments from the API
     * @param previousBatchNum
     * @return
     */
    private List<CoinSentiment> loadSentimentFromApi(Integer previousBatchNum) {
        JsonObject sentimentEntries = ApiUtils.getJson(this.BASE_URL, "auth", this.token);
        List<CoinSentiment> entries = extractEntitiesFromJson(sentimentEntries, previousBatchNum);

        return entries;
    }


    /**
     * Aggregates coins from JsonObject that is returned from API call.
     * The API doesn't provide price change or sentiment change data.
     * @param json
     * @return
     */
    private List<CoinSentiment> extractEntitiesFromJson(JsonObject json, Integer previousBatchNum) {
        List<CoinSentiment> coinSentiments = new ArrayList<>();

        Date currentDate = new Date();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            JsonObject info = entry.getValue().getAsJsonObject();

            CoinSentiment sentiment = new CoinSentiment(
                    entry.getKey(),
                    info.has("twitter_change_24h")
                        ? new BigDecimal(info.get("twitter_change_24h").toString())
                        : null,
                    info.has("twitter_volume_24h")
                        ? Long.parseLong(info.get("twitter_volume_24h").toString())
                        : null,
                    info.has("reddit_change_24h")
                        ? new BigDecimal(info.get("reddit_change_24h").toString())
                        : null,
                    info.has("reddit_volume_24h")
                        ? Long.parseLong(info.get("reddit_volume_24h").toString())
                        : null,
                    info.has("change_24h")
                        ? new BigDecimal(info.get("change_24h").toString())
                        : null,
                    info.has("volume_24h")
                        ? Long.parseLong(info.get("volume_24h").toString())
                        : null,
                    currentDate,
                    previousBatchNum + 1
            );

            coinSentiments.add(sentiment);
        }

        return coinSentiments;
    }

    /**
     * Save current snapshot of sentiments
     */
    public void saveSentiment() {
        // Find the previous batch number
        int previousBatchNum = SentimentRepository.findLastBatchNumber();

        List<CoinSentiment> sentiments = loadAllSentimentAnalysis(previousBatchNum);

        DbUtils.saveEntities(sentiments);
    }

    /**
     * Grab all sentiment analysis of coins and send Slack message for coins that meet a specific threshold
     */
    public void analyzeSentiments() {
        // Find the current batch number
        int currentBatchNum = SentimentRepository.findLastBatchNumber();

        List<CoinSentiment> currentBatch = SentimentRepository.findByBatchNum(currentBatchNum);

        List<CoinSentiment> filteredSentiments = currentBatch.stream()
                .filter(s -> s.getSocialVolumeChange_24h().compareTo(new BigDecimal(Constants.SOCIAL_VOLUME_CHANGE_THRESHOLD)) > 0)
                .sorted(Comparator.comparing((CoinSentiment c) -> c.getSocialVolumeChange_24h()).reversed())
                .collect(Collectors.toList());

        if (filteredSentiments.size() > 0) {
            SlackWebhook slack = new SlackWebhook(this.SLACK_ALERT_USERNAME);

            for (CoinSentiment sentiment : filteredSentiments) {
                String message = String.format("*%s* has an increase of %s%% in social volume.",
                        sentiment.getSymbol(), sentiment.getSocialVolumeChange_24h().setScale(2, RoundingMode.FLOOR).toString());

                slack.sendMessage(message);
            }

            slack.shutdown();
        }
    }


}
