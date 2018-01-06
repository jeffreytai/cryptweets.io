package com.crypto;

import com.crypto.sentiment.SolumeIO;

public class cryptweets {

    public static void main(String args[]) {

        // takes the current snapshot of coin market cap and adds it to the database
//        CoinMarketCap coinMarketCap = new CoinMarketCap(Constants.MINIMUM_COIN_RANK);
//        coinMarketCap.saveSnapshot();

        // retrieves sentiment analysis of all coins and adds it to the database
        SolumeIO solume = new SolumeIO();
        solume.saveSentiments();
    }
}
