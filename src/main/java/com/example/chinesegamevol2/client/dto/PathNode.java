package com.example.chinesegamevol2.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户端汉字匹配路径的每个节点
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PathNode {
    private int row;
    private int column;
    private String str;
}
