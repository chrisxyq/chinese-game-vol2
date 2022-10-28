package com.example.chinesegamevol2.server;

import com.example.chinesegamevol2.contract.senddata.SendDataToClientRequest;
import com.example.chinesegamevol2.contract.validate.ValidateMatchResultRequest;
import com.example.chinesegamevol2.contract.validate.ValidateMatchResultResponse;
import com.example.chinesegamevol2.utils.ChineseParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * Q、服务端保存汉字和偏旁的数据结构，并如何验证汉字组合正确？请用编码实现
 * 分两部分：
 * 1、服务端输出接口，比如客户端读取接口，服务端下发什么样的数据结构给前端？
 * 2、客户端上传接口，客户端上传什么样的数据结构给服务端，用来服务端验证？
 * 说明：
 * 1、参照上图游戏规则，设计服务端下发接口契约和客户端验证数据契约（需要实现具体拆解实例和验证过程），核心就是服务端字怎么拆，客户端怎么合并，最后怎么验证合并的是否正确。
 * 2、 限定一个字 最多只能分三块， 上中下， 左中右，或者如图所示 蠢字 上下下， 或者下上上，还有霸上左下右下等。形式不限。
 * 3、汉字示例：双、夸、蠢、从、鹏、朋、森、林、好、孙、骑、霸
 * 4、并非是需要罗列所有汉字，而是总结出汉字拆解和合并的规律
 * 5、给出前端要展示的偏旁部首
 */
@Slf4j
public class GameServer {
    /**
     * 服务端保存汉字和偏旁的数据结构
     * key为汉字
     * 值为偏旁列表
     */
    private Map<String, String[]> chineseToStrokeMap;
    /**
     * 服务端保存汉字和偏旁的数据结构
     * key为偏旁列表
     * 值为汉字
     */
    private Map<String, String>   strokeToChineseMap;

    public Map<String, String[]> getChineseToStrokeMap() {
        return chineseToStrokeMap;
    }

    public Map<String, String> getStrokeToChineseMap() {
        return strokeToChineseMap;
    }
    private String chineseList;

    public GameServer(String chineseList) {
        this.chineseList = chineseList;
        Pair<Map<String, String[]>, Map<String, String>> pair = ChineseParser.parseChineseList(chineseList);
        this.chineseToStrokeMap = pair.getLeft();
        this.strokeToChineseMap = pair.getRight();
    }

    /**
     * 1、服务端输出接口，服务端下发数据结构给前端
     *
     * @return
     * @throws Exception
     */
    public SendDataToClientRequest sendDataToClient() throws Exception {
        List<String> strokeList = new ArrayList<>();
        List<String[]> wordList = new ArrayList<>();
        for (String key : this.chineseToStrokeMap.keySet()) {
            String[] subList = this.chineseToStrokeMap.get(key);
            strokeList.addAll(Arrays.asList(subList));
            wordList.add(subList);
        }
        Collections.reverse(strokeList);
        return new SendDataToClientRequest(strokeList, wordList);
    }

    /**
     * 服务端校验客户端的匹配结果
     * 时间复杂度o(1)
     *
     * @param request
     * @return
     */
    public ValidateMatchResultResponse validateMatchResult(ValidateMatchResultRequest request) {
        if (request == null || request.getMatchResult() == null) {
            return new ValidateMatchResultResponse(false, null);
        }
        String chinese = this.strokeToChineseMap.getOrDefault(request.getMatchResult(), null);
        boolean res = StringUtils.isNotEmpty(chinese);
        log.info(String.format("服务端校验匹配结果为:%s，匹配字符为：%s", res, chinese));
        return new ValidateMatchResultResponse(res, chinese);
    }
}
