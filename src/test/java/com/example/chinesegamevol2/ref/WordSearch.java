package com.example.chinesegamevol2.ref;

import lombok.extern.slf4j.Slf4j;

/**
 * 79
 * Given a 2D board and a word, find if the word exists in the grid.
 *
 * The word can be constructed from letters of sequentially adjacent cell,
 * where "adjacent" cells are those horizontally or vertically neighboring. The same letter cell may not be used more than once.
 *
 * Example:
 *
 * board =
 * [
 *   ['A','B','C','E'],
 *   ['S','F','C','S'],
 *   ['A','D','E','E']
 * ]
 *
 * Given word = "ABCCED", return true.
 * Given word = "SEE", return true.
 * Given word = "ABCB", return false.
 */
@Slf4j
public class WordSearch {
    //对应不同方向遍历-->向右、向下、向左、向上
    static  int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    public static boolean exist(char[][] board, String word) {
        if (board == null || board.length == 0 || board[0].length == 0) return false;
        if (word.length() == 0) return true;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                int p = 0;
                if (word.charAt(0) == board[i][j])
                    if (dfs(board, i, j, word, p)) return true;
            }
        }
        return false;
    }

    /**
     *
     * @param board：当前面板
     * @param i：当前遍历的行
     * @param j：当前遍历的列
     * @param word：目标字符串
     * @param p：当前遍历到目标字符串的第几个字符
     * @return
     */
    private static boolean dfs(char[][] board, int i, int j, String word, int p) {
        if (p == word.length()) return true;
        if (i >= board.length || i < 0 || j >= board[0].length || j < 0 || board[i][j] != word.charAt(p)) return false;
        char temp = word.charAt(p);
        board[i][j] = '*';
        boolean flag = dfs(board, i, j+1, word, p+1) || dfs(board, i, j-1, word, p+1)
                || dfs(board, i+1, j, word, p+1) || dfs(board, i-1, j, word, p+1);
        if(flag){
            log.info(String.format("temp:%s,i:%s,j:%s",temp,i,j));
        }
        board[i][j] = temp;
        return flag;
    }

    public static void main(String[] args) {
        char board[][]={{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}};
        System.out.println(exist(board, "ABCCED"));
    }
}
