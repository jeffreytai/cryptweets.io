package com.crypto.orm.entity;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "currency_snapshot")
@Immutable
public class Currency {

    /**
     * Auto-generated Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long currencyId;

    /**
     * Full name of token
     */
    @Column(name = "Name", nullable = false)
    private String name;

    /**
     * Ticker (e.g. XRP, BTC)
     */
    @Column(name = "Symbol", nullable = false)
    private String symbol;

    /**
     * Rank by market cap
     */
    @Column(name = "Rank", nullable = false)
    private Integer rank;

    /**
     * Market cap in US dollars
     */
    @Column(name = "MarketCap", nullable = false)
    private BigDecimal marketCap;

    /**
     * Price in US dollars
     */
    @Column(name = "Price", nullable = false)
    private BigDecimal price;

    /**
     * Amount of tokens in circulation
     */
    @Column(name = "CirculatingSupply")
    private BigDecimal circulatingSupply;

    /**
     * Volume of tokens in the past 24 hours in US dollars
     */
    @Column(name = "Volume_24h")
    private BigDecimal volume_24h;

    /**
     * Percent change in 1 hour
     */
    @Column(name = "PercentChange_1h")
    private BigDecimal percentChange_1h;

    /**
     * Percent change in 24 hour
     */
    @Column(name = "PercentChange_24h")
    private BigDecimal percentChange_24h;

    /**
     * Percent change in 7 days
     */
    @Column(name = "PercentChange_7d")
    private BigDecimal percentChange_7d;

    /**
     * Time in which the entity was created
     */
    @Column(name = "Timestamp")
    private Date timestamp;

    /**
     * Batch/snapshot number
     */
    @Column(name = "BatchNum", nullable = false)
    private Integer batchNum;

    public Currency() {}

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

    @Override
    public boolean equals(Object o) {
        Currency c = (Currency) o;
        return this.getCurrencyId() == c.getCurrencyId();
    }

    @Override
    public int hashCode() {
        return name.hashCode() ^
                symbol.hashCode() ^
                rank.hashCode() ^
                marketCap.hashCode() ^
                price.hashCode() ^
                circulatingSupply.hashCode() ^
                volume_24h.hashCode() ^
                percentChange_1h.hashCode() ^
                percentChange_7d.hashCode() ^
                percentChange_24h.hashCode() ^
                timestamp.hashCode() ^
                batchNum.hashCode();
    }
}
