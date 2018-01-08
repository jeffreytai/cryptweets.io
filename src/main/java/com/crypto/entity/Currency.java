package com.crypto.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Currency {

    /**
     * Auto-generated Id
     */
    private Long currencyId;

    /**
     * Full name of token
     */
    private String name;

    /**
     * Ticker (e.g. XRP, BTC)
     */
    private String symbol;

    /**
     * Rank by market cap
     */
    private Integer rank;

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

    /**
     * Batch/snapshot number
     */
    private Integer batchNum;


    public Currency(String name, String symbol, Integer rank,
                    BigDecimal marketCap, BigDecimal price, BigDecimal circulatingSupply, BigDecimal volume_24h,
                    BigDecimal percentChange_1h, BigDecimal percentChange_24h, BigDecimal percentChange_7d,
                    Date timestamp, Integer batchNum) {
        this.name = name;
        this.symbol = symbol;
        this.rank = rank;
        this.marketCap = marketCap;
        this.price = price;
        this.circulatingSupply = circulatingSupply;
        this.volume_24h = volume_24h;
        this.percentChange_1h = percentChange_1h;
        this.percentChange_7d = percentChange_7d;
        this.percentChange_24h = percentChange_24h;
        this.timestamp = timestamp;
        this.batchNum = batchNum;
    }

    /**
     * Getters and setters for ORM
     */

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(BigDecimal circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public BigDecimal getVolume_24h() {
        return volume_24h;
    }

    public void setVolume_24h(BigDecimal volume_24h) {
        this.volume_24h = volume_24h;
    }

    public BigDecimal getPercentChange_1h() {
        return percentChange_1h;
    }

    public void setPercentChange_1h(BigDecimal percentChange_1h) {
        this.percentChange_1h = percentChange_1h;
    }

    public BigDecimal getPercentChange_24h() {
        return percentChange_24h;
    }

    public void setPercentChange_24h(BigDecimal percentChange_24h) {
        this.percentChange_24h = percentChange_24h;
    }

    public BigDecimal getPercentChange_7d() {
        return percentChange_7d;
    }

    public void setPercentChange_7d(BigDecimal percentChange_7d) {
        this.percentChange_7d = percentChange_7d;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(Integer batchNum) {
        this.batchNum = batchNum;
    }
}
