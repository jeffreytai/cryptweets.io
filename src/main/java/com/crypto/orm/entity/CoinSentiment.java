package com.crypto.orm.entity;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "CoinSentiment")
@Immutable
public class CoinSentiment {

    /**
     * Auto-generated Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long Id;

    /**
     * Coin symbol
     */
    @Column(name = "Symbol", nullable = false)
    private String symbol;

    /**
     * Percent of change in social volume from twitter in the past 24h
     */
    @Column(name = "TwitterChange_24h")
    private BigDecimal twitterChange_24h;

    /**
     * Last social volume from twitter in the past 24h
     */
    @Column(name = "TwitterVolume_24h")
    private Long twitterVolume_24h;

    /**
     * Percent of change in social volume from reddit in the past 24h
     */
    @Column(name = "RedditChange_24h")
    private BigDecimal redditChange_24h;

    /**
     * Last social volume from reddit in the past 24h
     */
    @Column(name = "RedditVolume_24h")
    private Long redditVolume_24h;

    /**
     * The percent of change in social volume from all sources in the past 24h
     */
    @Column(name = "SocialVolumeChange_24h")
    private BigDecimal socialVolumeChange_24h;

    /**
     * Last social volume from all sources in the past 24h
     */
    @Column(name = "SocialVolume_24h")
    private Long socialVolume_24h;

    /**
     * Timestamp
     */
    @Column(name = "Timestamp")
    private Date timestamp;

    /**
     * Batch/snapshot number
     */
    @Column(name = "BatchNum", nullable = false)
    private Integer batchNum;

    public CoinSentiment() {}

    public CoinSentiment(String symbol, BigDecimal twitterChange_24h, Long twitterVolume_24h,
                         BigDecimal redditChange_24h, Long redditVolume_24h,
                         BigDecimal socialVolumeChange_24h, Long socialVolume_24h,
                         Date timestamp, Integer batchNum) {
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

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
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
        CoinSentiment cs = (CoinSentiment) o;
        return this.getId() == cs.getId();
    }

    @Override
    public int hashCode() {
        return symbol.hashCode() ^
                twitterChange_24h.hashCode() ^
                twitterVolume_24h.hashCode() ^
                redditChange_24h.hashCode() ^
                redditVolume_24h.hashCode() ^
                socialVolumeChange_24h.hashCode() ^
                socialVolume_24h.hashCode() ^
                timestamp.hashCode() ^
                batchNum.hashCode();
    }

}
