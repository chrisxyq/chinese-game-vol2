package com.example.chinesegamevol2.client;

import com.example.chinesegamevol2.client.dto.MatchResult;
import com.example.chinesegamevol2.client.dto.PathNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class GameClient {
    /**
     * 对应不同方向遍历-->向上、向下、向左、向右
     */
    private static int[][]        DIRECTIONS   = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    /**
     * 二维数组棋盘：用于存放服务端发送的每个节点
     */
    private        String[][]     board        = null;
    /**
     * 存放能匹配到目标字符串的路径（节点列表）
     */
    private        List<PathNode> pathNodeList = new LinkedList<>();

    public GameClient(int boardRow, int boardColumn) {
        this.board = new String[boardRow][boardColumn];
    }

    public List<PathNode> getPathNodeList() {
        return pathNodeList;
    }

    public String[][] getBoard() {
        return board;
    }
    /**
     * 客户端逐个接收服务端发送的 Stroke
     *
     * @param i：Stroke           要放置的行
     * @param j：Stroke           要放置的列
     * @param stroke：当前要放置的        Stroke
     * @return
     */
    public void putStrokeToBoard(int i, int j,  String stroke) {
        log.info(String.format("客户端接收数据:%s，放置到第%s行，第%s列", stroke, i + 1, j + 1));
        //先把当前要放置的 StrokeNode节点放到客户端棋盘上
        board[i][j] = stroke;
    }
    /**
     * 客户端尝试匹配中文字chinese
     *
     * @param i：Stroke           要放置的行
     * @param j：Stroke           要放置的列
     * @param chinese：当前要匹配的目标汉字
     * @param p：当前遍历到目标字符串的第几个字符
     * @return
     */
    public MatchResult match(int i, int j, String[] chinese, int p) {
        //dfs遍历看是否能找到匹配成word的可行的路径，并将路径存放到 pathNodeList
        boolean res = dfs(i, j, chinese, p);
        StringBuilder pathStr = new StringBuilder();
        if (res) {
            pathStr = getStrFromPath();
            log.info(String.format("客户端找到合并汉字,匹配结果：%s",  pathStr));
        } else {
            log.info(String.format("客户端未找到合并汉字,待匹配汉字：%s",  Arrays.toString(chinese)));
        }
        return new MatchResult(pathNodeList, pathStr.toString());
    }

    private StringBuilder getStrFromPath() {
        StringBuilder pathStr = new StringBuilder();
        /**
         * 由于字体从按照上下左右的顺序写
         * 对pathNodeList中的节点，根据上下左右的顺序排序
         */
        pathNodeList = pathNodeList.stream().sorted(
                        Comparator.comparingInt(PathNode::getRow)
                                .thenComparingInt(PathNode::getColumn))
                .collect(Collectors.toList());
        for (PathNode nodeStr : pathNodeList) {
            pathStr.append(nodeStr.getStroke());
        }
        return pathStr;
    }

    /**
     * 由于是dfs，先打印的是最深层
     *
     * @param i：StrokeNode要放置的行
     * @param j：StrokeNode要放置的列
     * @param word：当前要匹配的目标汉字
     * @param p：当前遍历到目标字符串的第几个字符
     * @return
     */
    public boolean dfs(int i, int j, String[] word, int p) {
        if (p == word.length) return true;
        if (i >= board.length || i < 0
                || j >= board[0].length || j < 0
                || board[i][j] == null ||
                !Objects.equals(board[i][j], word[p])) {
            return false;
        }
        String temp = word[p];
        board[i][j] = "*";
        boolean flag = false;
        for (int index = 0; index < DIRECTIONS.length; index++) {
            flag = flag || dfs(i + DIRECTIONS[index][0], j + DIRECTIONS[index][1], word, p + 1);
            if (flag) {
                pathNodeList.add(new PathNode(i, j, temp));
                break;
            }
        }
        board[i][j] = temp;
        return flag;
    }

    /**
     * step4、匹配成功，客户端清除匹配结果
     *
     * @param pathNodeList
     */
    public void clearMatchResult(List<PathNode> pathNodeList) {
        if (!CollectionUtils.isEmpty(pathNodeList)) {
            for (PathNode pathNode : pathNodeList) {
                board[pathNode.getRow()][pathNode.getColumn()] = null;
            }
            this.pathNodeList = new LinkedList<>();
        }
    }

}
