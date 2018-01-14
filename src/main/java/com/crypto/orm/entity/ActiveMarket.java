package com.crypto.orm.entity;

import java.math.BigDecimal;

public class ActiveMarket {

    /**
     * Name of exchange
     */
    public String exchangeName;

    /**
     * Name of currency
     */
    public String currencyName;

    /**
     * Currency to base pair
     */
    public String pair;

    /**
     * Transacted volume in 24 hours
     */
    public BigDecimal volume_24h;

    /**
     * Price in USD
     */
    public Double price;

    /**
     * Volume percentage against all exchanges
     */
    public Double volumePercentage;

    /**
     * Updated recently
     */
    public String updated;

    public ActiveMarket(String exchangeName, String currencyName, String pair,
                        BigDecimal volume_24h, Double price, Double volumePercentage,
                        String updated) {
        this.exchangeName = exchangeName;
        this.currencyName = currencyName;
        this.pair = pair;
        this.volume_24h = volume_24h;
        this.price = price;
        this.volumePercentage = volumePercentage;
        this.updated = updated;
    }

    /**
     * Getters and setters
     */

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public BigDecimal getVolume_24h() {
        return volume_24h;
    }

    public void setVolume_24h(BigDecimal volume_24h) {
        this.volume_24h = volume_24h;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getVolumePercentage() {
        return volumePercentage;
    }

    public void setVolumePercentage(Double volumePercentage) {
        this.volumePercentage = volumePercentage;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        ActiveMarket am = (ActiveMarket) o;
        return this.getCurrencyName().equals(am.getCurrencyName());
    }

    @Override
    public int hashCode() {
        return exchangeName.hashCode() ^
                currencyName.hashCode() ^
                pair.hashCode() ^
                volume_24h.hashCode() ^
                price.hashCode() ^
                volumePercentage.hashCode();
    }
}
