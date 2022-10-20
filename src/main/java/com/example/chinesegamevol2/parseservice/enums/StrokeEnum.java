package com.example.chinesegamevol2.parseservice.enums;

import java.util.Objects;

/**
 * 笔画枚举
 */
@Deprecated
public enum StrokeEnum {
    HENG("横", "1"),
    SHU("竖", "2"),
    PIE("撇", "3"),
    NA("捺", "4"),
    HENGZHE("横折", "5"),
    DIAN("点", "6"),
    SHUGOU("竖钩", "7"),
    HENGPIE("横撇/横钩", "8"),
    OTHER("其他", "9");
    private final String chinese;
    private final String value;

    StrokeEnum(String chinese, String value) {
        this.chinese = chinese;
        this.value = value;
    }

    public String getChinese() {
        return chinese;
    }

    public String getValue() {
        return value;
    }

    public static StrokeEnum getByChinese(String chinese) {
        StrokeEnum anEnum = OTHER;
        for (StrokeEnum strokeEnum : StrokeEnum.values()) {
            if (Objects.equals(strokeEnum.getChinese(), chinese)) {
                anEnum = strokeEnum;
            }
        }
        return anEnum;
    }
}
