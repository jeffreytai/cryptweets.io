package com.crypto.repository;

import com.crypto.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {

    Currency findByName(String name);

    Currency findBySymbol(String symbol);

    @Query("select c from Currency c where c.BatchNum = ?1")
    List<Currency> findByBatchNum(Integer batchNum);
}
