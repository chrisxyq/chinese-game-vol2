package com.example.chinesegamevol2.client;


import com.example.chinesegamevol2.client.dto.MatchResult;
import com.example.chinesegamevol2.contract.StrokeNode;
import com.example.chinesegamevol2.client.dto.PathNode;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class GameClient {
    /**
     * 对应不同方向遍历-->向上、向下、向左、向右
     */
    private static int[][]           DIRECTIONS   = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    /**
     * 二维数组棋盘：用于存放服务端发送的每个节点
     */
    private        StrokeNode[][] board        = null;
    /**
     * 存放能匹配到目标字符串的路径（节点列表）
     */
    private        List<PathNode> pathNodeList = new LinkedList<>();

    public GameClient(int boardRow, int boardColumn) {
        this.board = new StrokeNode[boardRow][boardColumn];
    }

    public List<PathNode> getPathNodeList() {
        return pathNodeList;
    }

    public StrokeNode[][] getBoard() {
        return board;
    }

    /**
     * 客户端逐个接收服务端发送的 StrokeNode
     *
     * @param i：StrokeNode要放置的行
     * @param j：StrokeNode要放置的列
     * @param word：当前要匹配的目标汉字
     * @param p：当前遍历到目标字符串的第几个字符
     * @param node：当前要放置的        StrokeNode节点
     * @return
     */
    public MatchResult receiveDataFromServer(int i, int j, String[] word, int p, StrokeNode node) {
        System.out.println(String.format("客户端接收数据:%s，放置到第%s行，第%s列", node, i + 1, j + 1));
        //先把当前要放置的 StrokeNode节点放到客户端棋盘上
        board[i][j] = node;
        //dfs遍历看是否能找到匹配成word的可行的路径，并将路径存放到 pathNodeList
        boolean res = dfs(i, j, word, p);
        StringBuilder pathStr = new StringBuilder();
        if (res) {
            pathStr = getPathStrFromNodeList();
            System.out.println(String.format("客户端接收数据:%s找到合并汉字,匹配结果：%s", node, pathStr));
        } else {
            System.out.println(String.format("客户端接收数据:%s未找到合并汉字,当前匹配路径：%s,待匹配汉字：%s", node, pathNodeList,Arrays.toString(word)));
        }
        return new MatchResult(pathNodeList, pathStr.toString());
    }

    private StringBuilder getPathStrFromNodeList() {
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
            pathStr.append(nodeStr.getStr());
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
                !Objects.equals(board[i][j].getStrokeNum(), word[p])) {
            return false;
        }
        String temp = word[p];
        board[i][j] = new StrokeNode("*", board[i][j].getValidDirections());
        boolean flag = false;
        for (int index = 0; index < DIRECTIONS.length; index++) {
            if (board[i][j].getValidDirections()[index] == 0) {
                continue;
            }
            flag = flag || dfs(i + DIRECTIONS[index][0], j + DIRECTIONS[index][1], word, p + 1);
            if (flag) {
                pathNodeList.add(new PathNode(i, j, temp));
                break;
            }
        }
        board[i][j] = new StrokeNode(temp, board[i][j].getValidDirections());
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
