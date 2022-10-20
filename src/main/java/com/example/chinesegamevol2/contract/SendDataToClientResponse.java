package com.example.chinesegamevol2.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 服务端发送给客户端的数据结构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendDataToClientResponse {
    /**
     * 用于客户端接收的元素列表
     * 客户端棋盘存放的是Stroke
     */
    List<String> strokeList ;
    /**
     * 待匹配的目标中文列表
     * String[]为解析后的中文，每个元素为中文的偏旁部首
     */
    List<String[]>   wordList;
}
