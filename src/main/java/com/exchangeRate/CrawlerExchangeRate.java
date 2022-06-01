/*
 * Copyright (c) 2019-Now http://letright.com All rights reserved.
 */
package com.exchangeRate;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * 中国银行汇率定时获取
 *
 * @author zhangbaoshine
 * @version 2020-04-22
 */
public class CrawlerExchangeRate {
    private static final String POST_SEARCH_EN_URL = "https://srh.bankofchina.com/search/whpj/searchen.jsp";

    /**
     * 执行定时任务
     *
     * @param date 汇率日期
     */
    public void execute(String date) throws Exception {
        List<ExchangeRateInfo> listQuery = new ArrayList<>();
        // 设置最大请求页数为30页
        int maxPage = 30;
        for (int pageIndex = 1; pageIndex <= maxPage; pageIndex++) {
            // 抓取时间为lsToday,币别为sourceCurrency,页数为page的中国银行网页信息*/
            String searchEnHtml = getSearchEnHtml(date, "USD", String.valueOf(pageIndex));

            // 开始解析html中的汇率列表信息
            Map<Object, Object> map = assembleObjectByHtml(searchEnHtml, String.valueOf(pageIndex), listQuery);
            // 获取真实总页数
            String htmlPage = (String) map.get("page");
            listQuery = (List<ExchangeRateInfo>) map.get("list");

            // 当循环总页数大于查询到的真实页数时,则停止循环
            if (pageIndex > Integer.parseInt(htmlPage)) {
                break;
            }
        }
        System.out.println(listQuery.toString());
    }


    /**
     * 根据取得的网页,解析html中的内容
     *
     * @param html      要解析的html
     * @param pageIndex 循环的页数
     * @return map数据集合, 包含当前真实页数, 以及加上当前页的汇率数据
     */
    private Map<Object, Object> assembleObjectByHtml(String html, String pageIndex, List<ExchangeRateInfo> listQuery) {
        // 存储数据
        Map<Object, Object> map = new HashMap<>();

        // 使用Jsoup将html解析为Document对象
        Document document = Jsoup.parse(html);

        // 获取页面隐藏域中存放的当前页数
        Elements pageItem = document.getElementsByAttributeValue("name", "page");
        String htmlPage = pageItem.select("input[name=page]").val();
        map.put("page", htmlPage);

        // 获取页面的整个table信息,这个返回的页面基本上是返回多个table,下方需要细化处理
        Elements tables = document.getElementsByTag("table");

        // 设置存放汇率信息的table下标为-1(默认不存在)
        int tableIndex = -1;

        // 从table中循环获取,查找含有Currency Name字段的table
        for (int i = 0; i < tables.size(); i++) {
            Element element = tables.get(i);
            String text = element.text();
            // 找到含有汇率信息的table,给tableIndex赋值,跳出循环
            if (text.contains("Currency Name")) {
                tableIndex = i;
                break;
            }
        }
        // 判断当前循环页数是否大于真实页数
        boolean truePageFlag = Integer.parseInt(pageIndex) > Integer.parseInt(htmlPage);

        // 如果找到汇率列表信息,且当前页面不大于循环传入的页数时,才将实体对象放入list中
        if (tableIndex > -1 && !truePageFlag) {
            Element table = tables.get(tableIndex);
            // 遍历该表格内的所有的<tr> <tr/>
            Elements trs = table.select("tr");
            for (int i = 1; i < trs.size(); ++i) {
                ExchangeRateInfo exchangeRateInfo = new ExchangeRateInfo();
                Element tr = trs.get(i);
                Elements tds = tr.select("td");
                exchangeRateInfo.setCurrencyName(tds.get(0).text());
                exchangeRateInfo.setBuyingRate(Double.parseDouble(tds.get(1).text()));
                exchangeRateInfo.setCashBuyingRate(tds.get(2).text());
                exchangeRateInfo.setSellingRate(tds.get(3).text());
                exchangeRateInfo.setCashSellingRate(tds.get(4).text());
                exchangeRateInfo.setMiddleRate(Double.parseDouble(tds.get(5).text()));
                exchangeRateInfo.setPubTime(tds.get(6).text());
                listQuery.add(exchangeRateInfo);
            }
        }
        map.put("list", listQuery);
        return map;
    }

    /**
     * 获取整个网页的内容
     *
     * @param date           传入当前时间(格式:yyyy-MM-dd)或空
     * @param sourceCurrency 币别
     * @param pageIndex      当前查询页数(注意:此传入的页数可能大于页面真实页数)
     * @return 返回查询得到的整个网页内容
     */
    private String getSearchEnHtml(String date, String sourceCurrency, String pageIndex) throws IOException {
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.put("erectDate", date);
        parametersMap.put("nothing", date);
        parametersMap.put("pjname", sourceCurrency);
        parametersMap.put("page", pageIndex);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(POST_SEARCH_EN_URL);
        httpPost.setHeader("Accept", "*/*");

        HttpEntity entity = null;
        String htmlResult = "";
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : parametersMap.entrySet()) {
            nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nvps, "UTF-8");
        formEntity.setContentEncoding("UTF-8");
        httpPost.setEntity(formEntity);
        CloseableHttpResponse execute = httpClient.execute(httpPost);
        try {
            entity = execute.getEntity();
            htmlResult = EntityUtils.toString(entity, "UTF-8");
        } finally {
            EntityUtils.consume(entity);
            execute.close();
        }
        return htmlResult;
    }
}
