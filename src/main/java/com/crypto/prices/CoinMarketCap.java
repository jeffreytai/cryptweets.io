package com.crypto.prices;

import com.crypto.entity.Currency;
import com.crypto.builder.SessionFactoryBuilder;
import com.crypto.utils.DbUtils;
import com.crypto.utils.Utils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CoinMarketCap {


    private final int MINIMUM_COIN_RANK;

    public CoinMarketCap(int minimumCoinRank) {
        this.MINIMUM_COIN_RANK = minimumCoinRank;
    }

    /**
     * Grab all coins that has a minimum coin rank of 200 by market cap
     * @return
     */
    public List<Currency> loadCurrencies() {
        List<Currency> rankedCurrencies = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("https://coinmarketcap.com/all/views/all/").get();
            Elements currencies = doc.select("#currencies-all tbody tr");

            // initializing currentDate on the outside so that all currencies in the same batch/snapshot will have identical dates
            Date currentDate = new Date();

            // Grab the previous batch number
            int previousBatchNum = findLastBatchNumber();

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
                                Utils.sanitizeDecimalString(marketCap.text()),
                                new BigDecimal(currencyPrice.attr("data-usd")),
                                Utils.sanitizeDecimalString(circulatingSupply.text()),
                                Utils.sanitizeDecimalString(volume_24h.text()),
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
     * Grab the list of coins and save them to the database
     */
    public void saveSnapshot() {
        List<Currency> currencies = loadCurrencies();

        DbUtils.saveEntities(currencies);
    }

    /**
     * Returns the max batch number from the Currency table
     * @return
     */
    private Integer findLastBatchNumber() {
        Session session = SessionFactoryBuilder.getSessionFactory().openSession();
        Query query = session.createQuery("select MAX(c.batchNum) from Currency c");
        Object maxBatchNum = query.list().get(0);
        session.close();

        return maxBatchNum == null ? 0 : (Integer) maxBatchNum;
    }
}
