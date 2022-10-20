package com.example.chinesegamevol2.utils;

import com.example.chinesegamevol2.contract.dividechinese.DivideChineseResponse;
import com.example.chinesegamevol2.contract.dividechinese.DivideChineseResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class ChineseParser {
    /**
     * http://www.atoolbox.net/Tool.php?Id=956
     * 拆分中文字，获取中文字对应的偏旁部首的api
     */
    private static final String API_URL = "http://www.atoolbox.net/Api/GetChineseCharacterDismantling.php";

    //public static void main(String[] args) {
    //    log.info(JsonUtils.toJson(parseChineseList("霸夸")));
    //}

    public static Pair<Map<String, String[]>, Map<String, String>> parseChineseList(String chineseList) {
        Map<String, String[]> chineseToStrokeMap = new LinkedHashMap<>();
        Map<String, String> strokeToChineseMap = new LinkedHashMap<>();
        String responseString = postCalling(chineseList);
        DivideChineseResponse response = JsonUtils.deSerialize(responseString, DivideChineseResponse.class);
        if (response != null && response.getCode() == 200 && !CollectionUtils.isEmpty(response.getData())) {
            for (DivideChineseResponseData data : response.getData()) {
                String radicals = data.getRadicals();
                String[] strokeList = radicals
                        .replace("\n", "").split(" ");
                String aggregateStroke = radicals
                        .replace("\n", "").replace(" ", "");
                chineseToStrokeMap.put(data.getLetter(), strokeList);
                strokeToChineseMap.put(aggregateStroke, data.getLetter());
            }
        }
        return Pair.of(chineseToStrokeMap, strokeToChineseMap);
    }

    /**
     * post调用接口
     *
     * @param chinese
     * @return
     */
    public static String postCalling(String chinese) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_URL);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //设置请求的编码格式
        builder.setCharset(Charset.forName("utf-8"));
        //重新设置UTF-8编码，默认编码是ISO-8859-1
        ContentType contentType = ContentType.create("multipart/form-data", Charset.forName("UTF-8"));
        builder.addTextBody("ch", chinese, contentType);
        //简体中文
        builder.addTextBody("tf", "jt", contentType);
        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
            log.error("httpClient response 初始化异常" + e);
        } catch (IOException e) {
            log.error("httpClient response IO异常" + e);
        }
        HttpEntity responseEntity = response.getEntity();
        String sResponse = null;
        try {
            sResponse = EntityUtils.toString(responseEntity, "UTF-8");
        } catch (ParseException e) {
            log.error("httpClient 转换异常" + e);
        } catch (IOException e) {
            log.error("httpClient sResponse IO异常" + e);
        }
        log.info("httpPost 返回结果" + sResponse);
        return sResponse;
    }


}
