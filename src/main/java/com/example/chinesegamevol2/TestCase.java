package com.example.chinesegamevol2;

import com.example.chinesegamevol2.client.GameClient;
import com.example.chinesegamevol2.client.dto.MatchResult;
import com.example.chinesegamevol2.contract.SendDataToClientResponse;
import com.example.chinesegamevol2.server.GameServer;
import com.example.chinesegamevol2.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
public class TestCase {
    /**
     * 测试案例1：双
     *
     * @throws Exception
     */
    @Test
    public void test1() throws Exception {
        int[][] posList = {{2, 1}, {2, 0}};
        testProcedure("双", posList);
    }

    /**
     * 测试案例2：夸
     *
     * @throws Exception
     */
    @Test
    public void test2() throws Exception {
        int[][] posList = {{2, 1}, {1, 1}};
        testProcedure("夸", posList);
    }

    /**
     * 测试案例3：霸
     *
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        int[][] posList = {{2, 1}, {2, 0}, {1, 0}};
        testProcedure("霸", posList);
    }

    /**
     * 测试案例3：双、霸
     *
     * @throws Exception
     */
    @Test
    public void test4() throws Exception {
        int[][] posList = {{2, 1}, {2, 0}, {1, 0}, {2, 1}, {2, 0}};
        testProcedure("双霸", posList);
    }

    /**
     * @param chineseList:服务端存储的汉字
     * @param posList:模拟发给客户端的每个元素的放置地址
     * @throws Exception
     */
    public void testProcedure(String chineseList, int[][] posList) throws Exception {
        GameServer server = new GameServer();
        GameClient client = new GameClient(3, 3);
        //step1、服务端输出接口，服务端下发数据结构给前端
        SendDataToClientResponse response = server.sendDataToClient(chineseList);
        log.info(String.format("服务端下发数据结构给客户端:%s", JsonUtils.toJson(response)));
        //step2、客户端接收服务端发送数据，并合并汉字
        List<String[]> wordList = response.getWordList();
        List<String> strokeList = response.getStrokeList();
        for (int i = 0; i < strokeList.size(); i++) {
            for (String[] word : wordList) {
                MatchResult matchResult = client.receiveDataFromServer(
                        posList[i][0], posList[i][1], word, 0, strokeList.get(i));
                if (matchResult != null && StringUtils.hasLength(matchResult.getPathStr())) {
                    //step3、服务端校验客户端的匹配结果
                    boolean isValid = server.validateMatchResult(matchResult.getPathStr());
                    if (isValid) {
                        //step4、匹配成功，客户端清除匹配结果
                        client.clearMatchResult(matchResult.getPathNodeList());
                        break;
                    }
                }
            }
        }
    }
}
