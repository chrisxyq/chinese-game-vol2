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
    /**
     * 节点的行
     */
    private int row;
    /**
     * 节点的列
     */
    private int column;
    /**
     * 节点的偏旁部首
     */
    private String stroke;
}
