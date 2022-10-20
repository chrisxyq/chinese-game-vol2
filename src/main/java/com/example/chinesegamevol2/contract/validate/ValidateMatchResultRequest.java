package com.example.chinesegamevol2.contract.validate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户端请求服务端校验
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateMatchResultRequest {
    private String matchResult;
}
