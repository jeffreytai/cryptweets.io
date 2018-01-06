package com.crypto.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CoinSentiment {

    /**
     * Auto-generated Id
     */
    private Integer Id;

    /**
     * Coin symbol
     */
    private String symbol;

    /**
     * Percent of change in social volume from twitter in the past 24h
     */
    private BigDecimal twitterChange_24h;

    /**
     * Last social volume from twitter in the past 24h
     */
    private Long twitterVolume_24h;

    /**
     * Percent of change in social volume from reddit in the past 24h
     */
    private BigDecimal redditChange_24h;

    /**
     * Last social volume from reddit in the past 24h
     */
    private Long redditVolume_24h;

    /**
     * The percent of change in social volume from all sources in the past 24h
     */
    private BigDecimal socialVolumeChange_24h;

    /**
     * Last social volume from all sources in the past 24h
     */
    private Long socialVolume_24h;

    /**
     * Unix time format
     */
    private Long timestamp;

    /**
     * Batch/snapshot number
     */
    private Integer batchNum;


    public CoinSentiment(String symbol, BigDecimal twitterChange_24h, Long twitterVolume_24h,
                         BigDecimal redditChange_24h, Long redditVolume_24h,
                         BigDecimal socialVolumeChange_24h, Long socialVolume_24h,
                         Long timestamp, Integer batchNum) {
        this.symbol = symbol;
        this.twitterChange_24h = twitterChange_24h;
        this.twitterVolume_24h = twitterVolume_24h;
        this.redditChange_24h = redditChange_24h;
        this.redditVolume_24h = redditVolume_24h;
        this.socialVolumeChange_24h = socialVolumeChange_24h;
        this.socialVolume_24h = socialVolume_24h;
        this.timestamp = timestamp;
        this.batchNum = batchNum;
    }

    /**
     * Getters and setters for ORM
     */

    public Integer getId() {
        return Id;
    }

    public void setId(Integer Id) {
        this.Id = Id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getTwitterChange_24h() {
        return twitterChange_24h;
    }

    public void setTwitterChange_24h(BigDecimal twitterChange_24h) {
        this.twitterChange_24h = twitterChange_24h;
    }

    public Long getTwitterVolume_24h() {
        return twitterVolume_24h;
    }

    public void setTwitterVolume_24h(Long twitterVolume_24h) {
        this.twitterVolume_24h = twitterVolume_24h;
    }

    public BigDecimal getRedditChange_24h() {
        return redditChange_24h;
    }

    public void setRedditChange_24h(BigDecimal redditChange_24h) {
        this.redditChange_24h = redditChange_24h;
    }

    public Long getRedditVolume_24h() {
        return redditVolume_24h;
    }

    public void setRedditVolume_24h(Long redditVolume_24h) {
        this.redditVolume_24h = redditVolume_24h;
    }

    public BigDecimal getSocialVolumeChange_24h() {
        return socialVolumeChange_24h;
    }

    public void setSocialVolumeChange_24h(BigDecimal socialVolumeChange_24h) {
        this.socialVolumeChange_24h = socialVolumeChange_24h;
    }

    public Long getSocialVolume_24h() {
        return socialVolume_24h;
    }

    public void setSocialVolume_24h(Long socialVolume_24h) {
        this.socialVolume_24h = socialVolume_24h;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(Integer batchNum) {
        this.batchNum = batchNum;
    }
}
