package com.crypto;

import com.crypto.sentiment.SolumeIO;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class Application {

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);

        // takes the current snapshot of coin market cap and adds it to the database
//        CoinMarketCap coinMarketCap = new CoinMarketCap(Constants.MINIMUM_COIN_RANK);
//        coinMarketCap.saveSnapshot();

        // retrieves sentiment analysis of all coins and adds it to the database
//        SolumeIO solume = new SolumeIO();
//        solume.saveSentiments();
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
