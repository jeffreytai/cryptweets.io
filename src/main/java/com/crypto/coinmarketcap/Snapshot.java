package com.crypto.coinmarketcap;

import com.crypto.orm.HibernateUtil;
import com.crypto.utils.CurrencyUtils;
import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;

public class Snapshot {

    /**
     * Grab all coins that has a minimum coin rank of 200 by market cap
     */
    private static final int MINIMUM_COIN_RANK = 200;

    public static void takeSnapshot() {
        try {
            Document doc = Jsoup.connect("https://coinmarketcap.com/all/views/all/").get();
            Elements currencies = doc.select("#currencies-all tbody tr");

            if (currencies.size() > MINIMUM_COIN_RANK) {
                Session session = HibernateUtil.getSessionFactory().openSession();

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
                                CurrencyUtils.sanitizeDecimalString(marketCap.text()),
                                new BigDecimal(currencyPrice.attr("data-usd")),
                                CurrencyUtils.sanitizeDecimalString(circulatingSupply.text()),
                                CurrencyUtils.sanitizeDecimalString(volume_24h.text()),
                                percentChange_1h != null && percentChange_1h.hasAttr("data-usd")
                                        ? new BigDecimal(percentChange_1h.attr("data-usd"))
                                        : null,
                                percentChange_24h != null && percentChange_24h.hasAttr("data-usd")
                                        ? new BigDecimal(percentChange_24h.attr("data-usd"))
                                        : null,
                                percentChange_7d != null && percentChange_7d.hasAttr("data-usd")
                                        ? new BigDecimal(percentChange_7d.attr("data-usd"))
                                        : null
                        );

                        session.beginTransaction();
                        session.save(currency);
                        session.getTransaction().commit();

                    } catch (NullPointerException npe) {
                        continue;
                    }
                }

                HibernateUtil.shutdown();
                return;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
