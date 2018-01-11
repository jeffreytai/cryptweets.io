package com.crypto.prices;

import com.crypto.Constants;
import com.crypto.orm.entity.Currency;
import com.crypto.orm.repository.CurrencyRepository;
import com.crypto.slack.SlackWebhook;
import com.crypto.utils.DbUtils;
import com.crypto.utils.Utils;

import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class CoinMarketCap {

    /**
     * Minimum rank for the coin to be displayed
     */
    private final int MINIMUM_COIN_RANK;

    /**
     * Slack username to post as
     */
    private final String SLACK_ALERT_USERNAME = "coin-movement-alert";

    /**
     * Base URL for website
     */
    private final String BASE_URL = "https://coinmarketcap.com/all/views/all/";


    public CoinMarketCap(int minimumCoinRank) {
        this.MINIMUM_COIN_RANK = minimumCoinRank;
    }

    /**
     * Grab all coins that has a minimum coin rank of 200 by market cap
     * @return
     */
    public List<Currency> loadCurrencies(Integer previousBatchNum) {
        List<Currency> rankedCurrencies = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(this.BASE_URL).get();
            Elements currencies = doc.select("#currencies-all tbody tr");

            // initializing currentDate on the outside so that all currencies in the same batch/snapshot will have identical dates
            Date currentDate = new Date();

            if (currencies.size() > MINIMUM_COIN_RANK) {
                for (int i=MINIMUM_COIN_RANK; i<currencies.size(); i++) {
                    Element row = currencies.get(i);

                    try {
                        Element currencyName = row.getElementsByClass("currency-name-container").first();
                        Element currencySymbol = row.getElementsByClass("col-symbol").first();
                        Element currencyRank = row.getElementsByClass("text-center").first();
                        Element marketCap = row.getElementsByClass("market-cap").first();
                        Element currencyPrice = row.getElementsByClass("price").first();
                        Element circulatingSupply = row.getElementsByClass("circulating-supply").first();
                        Element volume_24h = row.getElementsByClass("volume").first();
                        Element percentChange_1h = row.getElementsByClass("percent-1h").first();
                        Element percentChange_24h = row.getElementsByClass("percent-24h").first();
                        Element percentChange_7d = row.getElementsByClass("percent-7d").first();

                        Currency currency = new Currency(
                                currencyName.text(),
                                currencySymbol.text(),
                                Integer.parseInt(currencyRank.text()),
                                Utils.sanitizeStringToBigDecimal(marketCap.text()),
                                new BigDecimal(currencyPrice.attr("data-usd")),
                                Utils.sanitizeStringToBigDecimal(circulatingSupply.text()),
                                Utils.sanitizeStringToBigDecimal(volume_24h.text()),
                                percentChange_1h != null && percentChange_1h.hasAttr("data-usd")
                                        ? new BigDecimal(percentChange_1h.attr("data-usd"))
                                        : null,
                                percentChange_24h != null && percentChange_24h.hasAttr("data-usd")
                                        ? new BigDecimal(percentChange_24h.attr("data-usd"))
                                        : null,
                                percentChange_7d != null && percentChange_7d.hasAttr("data-usd")
                                        ? new BigDecimal(percentChange_7d.attr("data-usd"))
                                        : null,
                                currentDate,
                                previousBatchNum + 1
                        );

                        rankedCurrencies.add(currency);

                    } catch (NullPointerException npe) {
                        continue;
                    }
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return rankedCurrencies;
    }

    /**
     * Grab the current list of coins from coin market cap.
     * If there was a previous batch of coins, check to see the movement in market cap
     * If the change in movement meets the threshold, notify the slack channel
     * @param saveCurrencies
     */
    public void analyzeCurrencies(boolean saveCurrencies) {
        // Find the previous batch number
        int previousBatchNum = findLastBatchNumber();

        List<Currency> currencies = loadCurrencies(previousBatchNum);

        if (previousBatchNum > 0) {
            List<Currency> previousBatch = CurrencyRepository.findByBatchNum(previousBatchNum);

            Set<Pair<Currency, Currency>> orderedCurrentToPreviousPair = new TreeSet<>(new Comparator<Pair<Currency, Currency>>() {
                @Override
                public int compare(Pair<Currency, Currency> o1, Pair<Currency, Currency> o2) {
                    BigDecimal o1growth = (o1.getKey().getPrice().subtract(o1.getValue().getPrice())).divide(o1.getValue().getPrice(), RoundingMode.HALF_UP);
                    BigDecimal o2growth = (o2.getKey().getPrice().subtract(o2.getValue().getPrice())).divide(o2.getValue().getPrice(), RoundingMode.HALF_UP);

                    return -1 * o1growth.compareTo(o2growth);
                }
            });

            for (Currency current : currencies) {
                Optional<Currency> previous = previousBatch.stream().filter(c -> c.getSymbol().equals(current.getSymbol())).findFirst();
                if (previous.isPresent()) {
                    Integer deltaRank = previous.get().getRank() - current.getRank();

                    if (deltaRank > Constants.RANK_CHANGE_THRESHOLD) {
                        try {
                            orderedCurrentToPreviousPair.add(new Pair<>(current, previous.get()));
                        } catch (ArithmeticException ex) {
                            continue;
                        }
                    }
                }
            }

            SlackWebhook slack = new SlackWebhook(this.SLACK_ALERT_USERNAME);

            for (Pair<Currency, Currency> pair : orderedCurrentToPreviousPair) {
                Currency current = pair.getKey();
                Currency previous = pair.getValue();

                Integer deltaRank = previous.getRank() - current.getRank();
                BigDecimal growth = (current.getPrice().subtract(previous.getPrice())).divide(previous.getPrice(), RoundingMode.HALF_UP);

                String message = String.format("%s (*%s*) moved up %d positions from %d to %d. " +
                    "The price went from $%s to $%s, a growth of %s%%.", current.getName(), current.getSymbol(),
                        deltaRank, previous.getRank(), current.getRank(),
                        previous.getPrice().setScale(2, RoundingMode.FLOOR).toString(), current.getPrice().setScale(2, RoundingMode.FLOOR).toString(),
                        growth.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.FLOOR).toString());

                slack.sendMessage(message);
            }

            slack.shutdown();
        }

        if (saveCurrencies)
            DbUtils.saveEntities(currencies);
    }

    /**
     * Returns the max batch number from the Currency table
     * @return
     */
    // TODO: Move this to a DbUtils function
    private Integer findLastBatchNumber() {
        Object maxBatchNum = DbUtils.runSingularResultQuery("select MAX(c.batchNum) from Currency c");

        return maxBatchNum == null ? 0 : (Integer) maxBatchNum;
    }
}
