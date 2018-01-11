package com.crypto.arbitrage;

import com.crypto.orm.entity.ActiveMarket;
import com.crypto.slack.SlackWebhook;
import com.crypto.utils.Utils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class MarketComparer {

    private final String SLACK_ALERT_USERNAME = "arbitrage-alert";
    private final String COIN_MARKET_CAP_EXCHANGE_BASE_URL = "https://coinmarketcap.com/exchanges/";

    private Integer ARBITRAGE_LIST_LIMIT = 5;

    private String baseExchange;
    private String arbitrageExchange;

    public MarketComparer(String baseExchange, String arbitrageExchange) {
        this.baseExchange = baseExchange;
        this.arbitrageExchange = arbitrageExchange;
    }

    /**
     * Find all active markets on both the base and arbitrage exchange.
     * Calculate the arbitrage rate (profit) between the two.
     */
    public void checkArbitrageRates() {

        try {
            Document baseExchangeDoc = Jsoup.connect(this.COIN_MARKET_CAP_EXCHANGE_BASE_URL + this.baseExchange).get();
            Elements baseExchangeMarkets = baseExchangeDoc.select("#markets tbody tr:not(:first-child)");
            List<ActiveMarket> baseActiveMarkets = extractActiveMarkets(baseExchange, baseExchangeMarkets);

            Document arbitrageExchangeDoc = Jsoup.connect(this.COIN_MARKET_CAP_EXCHANGE_BASE_URL + this.arbitrageExchange).get();
            Elements arbitrageExchangeMarkets = arbitrageExchangeDoc.select("#markets tbody tr:not(:first-child)");
            List<ActiveMarket> arbitrageActiveMarkets = extractActiveMarkets(arbitrageExchange, arbitrageExchangeMarkets);

            SlackWebhook slack = new SlackWebhook(this.SLACK_ALERT_USERNAME);

            // Order the base-arbitrage pairs as they are added and maintain a list of added pairs so there are no duplicates
            Set<Triple<ActiveMarket, Double, ActiveMarket>> orderedBaseToArbitrageTuple = new TreeSet<>(new Comparator<Triple<ActiveMarket, Double, ActiveMarket>>() {
                @Override
                public int compare(Triple<ActiveMarket, Double, ActiveMarket> o1, Triple<ActiveMarket, Double, ActiveMarket> o2) {
                    return -1 * o1.getMiddle().compareTo(o2.getMiddle());
                }
            });

            Map<String, Double> addedPairs = new HashMap<>();

            // Loop through each option and add it to the ordered set. If the base currency already exists
            // show the pairing with the higher arbitrage rate
            for (ActiveMarket baseMarket : baseActiveMarkets) {
                Optional<ActiveMarket> potentialMarket = arbitrageActiveMarkets.stream().filter(m -> m.getCurrencyName().equals(baseMarket.getCurrencyName())).findFirst();

                if (potentialMarket.isPresent()) {
                    Double basePrice = baseMarket.getPrice();
                    Double arbitragePrice = potentialMarket.get().getPrice();
                    Double arbitrageRate = (arbitragePrice - basePrice) / basePrice;

                    if (arbitragePrice > basePrice) {
                        if (!addedPairs.containsKey(baseMarket.getCurrencyName())) {
                            orderedBaseToArbitrageTuple.add(new ImmutableTriple<>(baseMarket, arbitrageRate, potentialMarket.get()));
                            addedPairs.put(baseMarket.getCurrencyName(), basePrice);
                        }
                        else {
                            Boolean removed = orderedBaseToArbitrageTuple.removeIf(p -> p.getLeft().equals(baseMarket) && p.getMiddle() < arbitrageRate);
                            if (removed) {
                                orderedBaseToArbitrageTuple.add(new ImmutableTriple<>(baseMarket, arbitrageRate, potentialMarket.get()));
                                addedPairs.put(baseMarket.getCurrencyName(), basePrice);
                            }
                        }
                    }
                }
            }

            // Send information messages
            if (orderedBaseToArbitrageTuple.size() > 0) {
                int iteration = 0;

                for (Triple<ActiveMarket, Double, ActiveMarket> orderedBaseToArbitrageEntry : orderedBaseToArbitrageTuple) {
                    ActiveMarket base = orderedBaseToArbitrageEntry.getLeft();
                    ActiveMarket arb = orderedBaseToArbitrageEntry.getRight();
                    Double arbitragePercent = orderedBaseToArbitrageEntry.getMiddle();

                    String message = String.format("%s (trading pair *%s*) - %s price: $%s / %s price: $%s. Arbitrage rate: %s%%",
                            base.getCurrencyName(), base.getPair(), base.getExchangeName(), base.getPrice().toString(),
                            arb.getExchangeName(), arb.getPrice().toString(), Utils.roundDecimal(arbitragePercent * 100).toString());

                    slack.sendMessage(message);

                    if (++iteration > this.ARBITRAGE_LIST_LIMIT)
                        break;
                }
            }

            slack.shutdown();
        } catch (IOException ex) {
            System.err.println("Exchange not found on CoinMarketCap");
            ex.printStackTrace();
        }
    }

    private List<ActiveMarket> extractActiveMarkets(String exchange, Elements markets) {
        List<ActiveMarket> activeMarkets = new ArrayList<>();

        for (Element row : markets) {
            Element currencyName = row.getElementsByClass("market-name").first();
            Element pair = row.getElementsByClass("market-name").first().parent().nextElementSibling();
            Element volume_24h = row.getElementsByClass("volume").first();
            Element price = row.getElementsByClass("price").first();
            Element volumePercentage = row.getElementsByClass("price").first().parent().nextElementSibling();
            Element updated = row.getElementsByClass("price").first().parent().lastElementSibling();

            ActiveMarket activeMarket = new ActiveMarket(
                    exchange,
                    currencyName.text(),
                    pair.text(),
                    new BigDecimal(volume_24h.attr("data-usd")),
                    Utils.sanitizeStringToDouble(price.text()),
                    Utils.sanitizeStringToDouble(volumePercentage.text()),
                    updated.text()
            );

            activeMarkets.add(activeMarket);
        }

        return activeMarkets;
    }
}
