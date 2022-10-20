package com.example.chinesegamevol2.contract.validate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateMatchResultResponse {
    private boolean isSuccess;
    private String chinese;
}
