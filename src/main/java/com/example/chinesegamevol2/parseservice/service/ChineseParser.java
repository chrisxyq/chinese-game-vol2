package com.example.chinesegamevol2.parseservice.service;

import com.example.chinesegamevol2.contract.StrokeNode;
import com.example.chinesegamevol2.server.dto.ParsedNode;

import java.util.*;

/**
 * 读取中文字
 * 得到 服务端下发给前端的数据结构
 */
public class ChineseParser implements IChineseParser {

    public static Map<String, ParsedNode> parseChineseList(List<String> list) throws Exception {
        Map<String, ParsedNode> map = new LinkedHashMap<>();
        for (String chinese : list) {
            map.putAll(parseChinese(chinese));
        }
        return map;
    }

    public static Map<String, ParsedNode> parseChinese(String chinese) throws Exception {
        Map<String, ParsedNode> map = new LinkedHashMap<>();
        String key = StrokeComputer.getStrokeNum(chinese);
        List<StrokeNode> nodeList = new ArrayList<>();
        ParsedNode parsedNode = null;
        switch (chinese) {
            case "双":
                nodeList.add(new StrokeNode("86", new int[]{0, 0, 0, 1}));
                nodeList.add(new StrokeNode(StrokeComputer.getStrokeNum("又"), new int[]{0, 0, 1, 0}));
                parsedNode = new ParsedNode(nodeList, new String[]{"86",
                        StrokeComputer.getStrokeNum("又")});
                break;
            case "夸":
                nodeList.add(new StrokeNode(StrokeComputer.getStrokeNum("大"), new int[]{0, 1, 0, 0}));
                nodeList.add(new StrokeNode(StrokeComputer.getStrokeNum("亏"), new int[]{1, 0, 0, 0}));
                parsedNode = new ParsedNode(nodeList, new String[]{StrokeComputer.getStrokeNum("大"),
                        StrokeComputer.getStrokeNum("亏")});
                break;
            case "霸":
                nodeList.add(new StrokeNode("16826666", new int[]{0, 1, 0, 0}));
                nodeList.add(new StrokeNode(StrokeComputer.getStrokeNum("革"), new int[]{1, 0, 0, 1}));
                nodeList.add(new StrokeNode(StrokeComputer.getStrokeNum("月"), new int[]{0, 0, 1, 0}));
                parsedNode = new ParsedNode(nodeList, new String[]{"16826666",
                        StrokeComputer.getStrokeNum("革"),StrokeComputer.getStrokeNum("月")});
                break;
            default:
                throw new UnsupportedOperationException("暂不支持该中文");
        }
        map.put(key, parsedNode);
        return map;
    }
}
