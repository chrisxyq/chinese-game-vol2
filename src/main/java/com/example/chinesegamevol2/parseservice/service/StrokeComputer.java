package com.example.chinesegamevol2.parseservice.service;

import com.example.chinesegamevol2.parseservice.enums.StrokeEnum;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * 获取中文笔画顺序及笔数
 */
public class StrokeComputer {
    private static String COLON = "：";
    private static String COMMA = ",";

    /**
     * 获取中文字对应的笔顺字符
     * @param str
     * @return
     * @throws Exception
     */
    public static String getStrokeNum(String str) throws Exception {
        StringBuilder res = new StringBuilder();
        String biShunAndBiShu = getBiShunAndBiShu(str);
        //  我字共7画；我字的笔顺：撇,横,竖钩,提,斜钩,撇,点
        String biShun = biShunAndBiShu.substring(biShunAndBiShu.indexOf(COLON) + 1);
        String[] split = biShun.split(COMMA);
        for (String s : split) {
            StrokeEnum anEnum = StrokeEnum.getByChinese(s);
            res.append(anEnum.getValue());
        }
        return res.toString();
    }

    public static String getBiShunAndBiShu(String str) throws Exception {
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(10);
        connectionManager.getParams().setConnectionTimeout(300000000);
        connectionManager.getParams().setSoTimeout(300000000);
        HttpClient client = new HttpClient(connectionManager);
        GetMethod method = new GetMethod("http://bishun.shufaji.com/" + cnToUnicode(str) + ".html");
        client.executeMethod(method);
        String returnJson = new String(method.getResponseBody(), "utf-8");
        int idx1 = returnJson.indexOf("<div id=\"hzcanvas\">");
        if (idx1 != -1) {
            idx1 += 19;
            int idx2 = returnJson.indexOf("</div>", idx1);
            returnJson = returnJson.substring(idx1, idx2 == -1 ? returnJson.length() : idx2);
        }
        return returnJson;
    }

    /**
     * 中文转Unicode
     *
     * @param cn
     * @return
     */
    private static String cnToUnicode(String cn) {
        char[] chars = cn.toCharArray();
        StringBuilder returnStr = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            returnStr.append("0x").append(Integer.toString(chars[i], 16));
        }
        return returnStr.toString();
    }

    //public static void main(String[] args) throws Exception {
    //    String str = "我爱你中国";
    //    System.out.println(str);
    //    for (int i = 0; i < str.length(); i++) {
    //        System.out.println(getBiShunAndBiShu(str.substring(i, i + 1)));
    //    }
    //}
}