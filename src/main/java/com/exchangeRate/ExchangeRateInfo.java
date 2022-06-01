/**
 * Copyright (c) 2019-Now http://letright.com All rights reserved.
 */
package com.exchangeRate;

/**
 * 中国银行html汇率查询数据映射Entity
 *
 * @author zhangbaoshine
 * @version 2020-04-22
 */
public class ExchangeRateInfo {

    /**
     * 币别
     */
    private String currencyName;
    /**
     * 买入汇率
     */
    private Double buyingRate;
    /**
     * 现金买入汇率
     */
    private String cashBuyingRate;
    /**
     * 卖出汇率
     */
    private String sellingRate;
    /**
     * 现金卖出汇率
     */
    private String cashSellingRate;
    /**
     * 中间价
     */
    private Double middleRate;
    /**
     * 时间
     */
    private String pubTime;

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Double getBuyingRate() {
        return buyingRate;
    }

    public void setBuyingRate(Double buyingRate) {
        this.buyingRate = buyingRate;
    }

    public String getCashBuyingRate() {
        return cashBuyingRate;
    }

    public void setCashBuyingRate(String cashBuyingRate) {
        this.cashBuyingRate = cashBuyingRate;
    }

    public String getSellingRate() {
        return sellingRate;
    }

    public void setSellingRate(String sellingRate) {
        this.sellingRate = sellingRate;
    }

    public String getCashSellingRate() {
        return cashSellingRate;
    }

    public void setCashSellingRate(String cashSellingRate) {
        this.cashSellingRate = cashSellingRate;
    }

    public Double getMiddleRate() {
        return middleRate;
    }

    public void setMiddleRate(Double middleRate) {
        this.middleRate = middleRate;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    @Override
    public String toString() {
        return "ExchangeRateInfo{" +
                "currencyName='" + currencyName + '\'' +
                ", buyingRate=" + buyingRate +
                ", cashBuyingRate='" + cashBuyingRate + '\'' +
                ", sellingRate='" + sellingRate + '\'' +
                ", cashSellingRate='" + cashSellingRate + '\'' +
                ", middleRate=" + middleRate +
                ", pubTime='" + pubTime + '\'' +
                '}';
    }
}
