package com.crypto.orm.repository;

import com.crypto.orm.entity.Currency;
import com.crypto.utils.DbUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyRepository {

    public static List<Currency> findByBatchNum(Integer batchNum) {
        String query = "SELECT c FROM Currency c WHERE c.batchNum = :batchNum";
        Map<Object, Object> bindedParameters = new HashMap<>();
        bindedParameters.put("batchNum", batchNum);

        List<Currency> result = (List<Currency>) DbUtils.runMultipleResultQuery(query, bindedParameters);
        return result;
    }
}
