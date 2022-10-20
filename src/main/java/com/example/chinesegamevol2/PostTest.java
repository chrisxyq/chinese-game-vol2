package com.example.chinesegamevol2;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
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

import java.io.*;
import java.nio.charset.Charset;

@Slf4j
public class PostTest {

    public static void main(String[] args)  {
        //设置请求参数
        //String apiUri = "http://www.atoolbox.net/Api/GetChineseCharacterDismantling.php";
        //String responseString = httpPost(apiUri, "夸");
        //System.out.println(responseString);


        System.out.println(StringEscapeUtils.escapeJava("大亏"));
    }

    public static String httpPost(String url, String data) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName("utf-8"));//设置请求的编码格式
        ContentType contentType = ContentType.create("multipart/form-data", Charset.forName("UTF-8"));//重新设置UTF-8编码，默认编码是ISO-8859-1
//        builder.addTextBody("jmsg", data, ContentType.MULTIPART_FORM_DATA);
        builder.addTextBody("ch", data, contentType);
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
