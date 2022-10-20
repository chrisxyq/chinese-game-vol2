package com.example.chinesegamevol2.contract.divideChinese;

import lombok.Data;

import java.util.List;

/**
 * 拆分中文汉字的返回体
 */
@Data
public class DivideChineseResponse {
    private int    code;
    private String msg;
    private List<DivideChineseResponseData>   data;
}
