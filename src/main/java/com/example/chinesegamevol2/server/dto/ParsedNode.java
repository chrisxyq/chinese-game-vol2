package com.example.chinesegamevol2.server.dto;

import com.example.chinesegamevol2.contract.StrokeNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 服务端对一个汉字的解析结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedNode {
    private List<StrokeNode> strokeNodeList;
    private String[]         word;
}
