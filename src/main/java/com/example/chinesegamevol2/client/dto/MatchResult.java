package com.example.chinesegamevol2.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 客户端的汉字匹配结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchResult {
    /**
     * 客户端的汉字匹配路径
     */
    private List<PathNode> pathNodeList;
    /**
     * 客户端匹配得到的汉字
     */
    private String         pathStr;
}
