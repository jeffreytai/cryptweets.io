package com.crypto;

import com.crypto.arbitrage.MarketComparer;
import com.crypto.orm.HibernateUtils;
import com.crypto.prices.CoinMarketCap;
import com.crypto.sentiment.SolumeIO;
import com.google.common.base.Strings;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class Application {

    // TODO: Import connection pooling (hibernate-c3p0)
    // TODO: Write unit tests
    public static void main(String args[]) {
//        Used when running locally
//        String saveSnapshot = System.getenv("saveSnapshot");
//        String saveSentiment = System.getenv("saveSentiment");

        // Used for executable jar
        String saveSnapshot = System.getProperty("saveSnapshot");
        String saveSentiment = System.getProperty("saveSentiment");

        if (Strings.isNullOrEmpty(saveSnapshot) || Strings.isNullOrEmpty(saveSentiment)) {
            System.out.println("saveSnapshot and saveSentiment properties not provided");
            System.exit(1);
        }

//        SpringApplication.run(Application.class, args);

        // takes the current snapshot of coin market cap and adds it to the database
        CoinMarketCap coinMarketCap = new CoinMarketCap(Constants.MINIMUM_COIN_RANK);
        if (saveSnapshot.toLowerCase().equals("yes")) {
            coinMarketCap.saveCurrencySnapshot();
        }

        coinMarketCap.analyzeCurrencies();

        // retrieves sentiment analysis of all coins and adds it to the database
        SolumeIO solume = new SolumeIO();
        if (saveSentiment.toLowerCase().equals("yes")) {
            solume.saveSentiment();
        }

        solume.analyzeSentiments();


        // checks arbitrage opportunies between 2 exchanges
        MarketComparer marketComparer = new MarketComparer("binance", "bithumb");
//        marketComparer.checkArbitrageRates();

        // Close database connections
        HibernateUtils.shutdown();
    }

    /**
     * Print to console the beans that are loaded by Spring Boot
     * @param ctx
     * @return
     */
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }
        };
    }
}
