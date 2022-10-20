package com.example.chinesegamevol2.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务端下发给前端的数据结构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrokeNode {
    /**
     * 中文对应的笔画字符
     */
    private String strokeNum;
    /**
     * 中文对应的上下左右的可行路径
     */
    private int[] validDirections;
}
