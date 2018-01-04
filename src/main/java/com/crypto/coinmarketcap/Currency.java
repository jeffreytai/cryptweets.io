package com.crypto.coinmarketcap;

import java.math.BigDecimal;
import java.util.Date;

public class Currency {

    /**
     * Full name of token
     */
    private String name;

    /**
     * Ticker (e.g. XRP, BTC)
     */
    private String symbol;

    /**
     * Market cap in US dollars
     */
    private BigDecimal marketCap;

    /**
     * Price in US dollars
     */
    private BigDecimal price;

    /**
     * Amount of tokens in circulation
     */
    private BigDecimal circulatingSupply;

    /**
     * Volume of tokens in the past 24 hours in US dollars
     */
    private BigDecimal volume_24h;

    /**
     * Percent change in 1 hour
     */
    private BigDecimal percentChange_1h;

    /**
     * Percent change in 24 hour
     */
    private BigDecimal percentChange_24h;

    /**
     * Percent change in 7 days
     */
    private BigDecimal percentChange_7d;

    /**
     * Time in which the entity was created
     */
    private Date timestamp;


    public Currency(String name, String symbol, BigDecimal marketCap, BigDecimal price, BigDecimal circulatingSupply, BigDecimal volume_24h, BigDecimal percentChange_1h, BigDecimal percentChange_24h, BigDecimal percentChange_7d) {
        this.name = name;
        this.symbol = symbol;
        this.marketCap = marketCap;
        this.price = price;
        this.circulatingSupply = circulatingSupply;
        this.volume_24h = volume_24h;
        this.percentChange_1h = percentChange_1h;
        this.percentChange_7d = percentChange_7d;
        this.percentChange_24h = percentChange_24h;
        this.timestamp = new Date();
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }
}
