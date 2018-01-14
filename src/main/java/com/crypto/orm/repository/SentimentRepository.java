package com.crypto.orm.repository;

import com.crypto.orm.entity.CoinSentiment;
import com.crypto.utils.DbUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SentimentRepository {

    /**
     * Retrieves all CoinSentiments in the specified batch
     * @param batchNum
     * @return
     */
    public static List<CoinSentiment> findByBatchNum(Integer batchNum) {
        String query = "SELECT c FROM CoinSentiment c WHERE c.batchNum = :batchNum";
        Map<Object, Object> bindedParameters = new HashMap<>();
        bindedParameters.put("batchNum", batchNum);

        List<CoinSentiment> result = (List<CoinSentiment>) DbUtils.runMultipleResultQuery(query, bindedParameters);
        return result;
    }

    /**
     * Returns the max batch number from the CoinSentiment table
     * @return
     */
    public static Integer findLastBatchNumber() {
        Object maxBatchNum = DbUtils.runSingularResultQuery("select MAX(c.batchNum) from CoinSentiment c");

        return maxBatchNum == null ? 0 : (Integer) maxBatchNum;
    }
}
