package com.crypto.arbitrage;

import com.crypto.orm.entity.ActiveMarket;
import com.crypto.slack.SlackWebhook;
import com.crypto.utils.Utils;
import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MarketComparer {

    private final String SLACK_ALERT_USERNAME = "arbitrage-alert";
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
            Document baseExchangeDoc = Jsoup.connect("https://coinmarketcap.com/exchanges/" + baseExchange).get();
            Elements baseExchangeMarkets = baseExchangeDoc.select("#markets tbody tr:not(:first-child)");
            List<ActiveMarket> baseActiveMarkets = extractActiveMarkets(baseExchange, baseExchangeMarkets);

            Document arbitrageExchangeDoc = Jsoup.connect("https://coinmarketcap.com/exchanges/" + arbitrageExchange).get();
            Elements arbitrageExchangeMarkets = arbitrageExchangeDoc.select("#markets tbody tr:not(:first-child)");
            List<ActiveMarket> arbitrageActiveMarkets = extractActiveMarkets(arbitrageExchange, arbitrageExchangeMarkets);

            SlackWebhook slack = new SlackWebhook(this.SLACK_ALERT_USERNAME);

            Set<Pair<ActiveMarket, ActiveMarket>> orderedBaseToArbitragePair = new TreeSet<>(new Comparator<Pair<ActiveMarket, ActiveMarket>>() {
                @Override
                public int compare(Pair<ActiveMarket, ActiveMarket> o1, Pair<ActiveMarket, ActiveMarket> o2) {
                    Double firstProfitPercent = o1.getValue().getPrice() - o1.getKey().getPrice();
                    Double secondProfitPercent = o2.getValue().getPrice() - o2.getKey().getPrice();

                    return firstProfitPercent.compareTo(secondProfitPercent);
                }
            });

            for (ActiveMarket baseMarket : baseActiveMarkets) {
                Optional<ActiveMarket> potentialMarket = arbitrageActiveMarkets.stream().filter(m -> m.getCurrencyName().equals(baseMarket.getCurrencyName())).findFirst();

                if (potentialMarket.isPresent()) {
                    Double basePrice = baseMarket.getPrice();
                    Double arbitragePrice = potentialMarket.get().getPrice();

                    if (arbitragePrice > basePrice) {
                        orderedBaseToArbitragePair.add(new Pair<ActiveMarket, ActiveMarket>(baseMarket, potentialMarket.get()));
                    }
                }
            }

            if (orderedBaseToArbitragePair.size() > 0) {
                Set<Pair<ActiveMarket, ActiveMarket>> limitedBaseToArbitragePair = orderedBaseToArbitragePair.stream().limit(3).collect(Collectors.toSet());

                for (Pair<ActiveMarket, ActiveMarket> orderedBaseToArbitrageEntry : limitedBaseToArbitragePair) {
                    ActiveMarket base = orderedBaseToArbitrageEntry.getKey();
                    ActiveMarket arb = orderedBaseToArbitrageEntry.getValue();
                    Double arbitragePercent = (arb.getPrice() - base.getPrice()) / base.getPrice();

                    String message = String.format("%s (trading pair %s) - %s price: $%s / %s price: $%s. Arbitrage rate: %s%%",
                            base.getCurrencyName(), base.getPair(), base.getExchangeName(), base.getPrice().toString(),
                            arb.getExchangeName(), arb.getPrice().toString(), Utils.roundDecimal(arbitragePercent * 100).toString());

                    slack.sendMessage(message);
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
