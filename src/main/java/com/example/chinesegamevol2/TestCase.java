package com.example.chinesegamevol2;

import com.example.chinesegamevol2.client.GameClient;
import com.example.chinesegamevol2.client.dto.MatchResult;
import com.example.chinesegamevol2.contract.senddata.SendDataToClientRequest;
import com.example.chinesegamevol2.contract.validate.ValidateMatchResultRequest;
import com.example.chinesegamevol2.contract.validate.ValidateMatchResultResponse;
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
     * 假设服务端有k个中文字
     * 中文字最大不超过t个偏旁部首
     * 四个方向遍历
     * 客户端的棋盘格为：m*n
     * 1.若服务端按倒序的顺序发放偏旁部首：
     * 客户端每次进行中文匹配的时间复杂度：k*3**(t-1)
     * 2.若服务端按乱序的顺序发放偏旁部首：
     * 客户端每次进行中文匹配的时间复杂度：m*n*k*4*3**(t-2)
     * ======
     * 服务端校验的时间复杂度：o(1)
     *
     * @param chineseList:服务端存储的汉字
     * @param posList:模拟发给客户端的每个元素的放置地址
     * @throws Exception
     */
    public void testProcedure(String chineseList, int[][] posList) throws Exception {
        GameServer server = new GameServer(chineseList);
        GameClient client = new GameClient(3, 3);
        //step1、服务端输出接口，服务端下发数据结构给客户端
        SendDataToClientRequest request = server.sendDataToClient();
        log.info(String.format("服务端下发数据结构给客户端:%s", JsonUtils.toJson(request)));
        //step2、客户端接收服务端发送数据，并合并汉字
        List<String[]> wordList = request.getWordList();
        List<String> strokeList = request.getStrokeList();
        for (int i = 0; i < strokeList.size(); i++) {
            //step2、客户端接收服务端发送数据
            client.putStrokeToBoard(posList[i][0], posList[i][1], strokeList.get(i));
            //step3、客户端尝试合并汉字
            for (String[] word : wordList) {
                MatchResult matchResult = client.match(posList[i][0], posList[i][1], word, 0);
                if (matchResult != null && StringUtils.hasLength(matchResult.getPathStr())) {
                    //step4、客户端合并之后，请求服务端校验客户端的匹配结果
                    ValidateMatchResultResponse response = server.validateMatchResult(new ValidateMatchResultRequest(matchResult.getPathStr()));
                    if (response != null && response.isSuccess()) {
                        //step5、服务端校验通过，匹配成功，客户端清除匹配结果
                        client.clearMatchResult(matchResult.getPathNodeList());
                        break;
                    }
                }
            }
        }
    }
}
