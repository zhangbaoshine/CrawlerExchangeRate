package com.exchangeRate;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.Test;
@Ignore
public class ExchangeRateTest {
    @Test
    public void test1() {
        CrawlerExchangeRate crawlerExchangeRate = new CrawlerExchangeRate();
        try {
            crawlerExchangeRate.execute("2022-06-01");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
