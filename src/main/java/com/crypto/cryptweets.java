package com.crypto;

import com.crypto.prices.CoinMarketCap;
import com.crypto.utils.Constants;

public class cryptweets {

    public static void main(String args[]) {
        CoinMarketCap coinMarketCap = new CoinMarketCap(Constants.MINIMUM_COIN_RANK);

        // takes the current snapshot of coin market cap and adds it to the database
        coinMarketCap.saveSnapshot();
    }
}
