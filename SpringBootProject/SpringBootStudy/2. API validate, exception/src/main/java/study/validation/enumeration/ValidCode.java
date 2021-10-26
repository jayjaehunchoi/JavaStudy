package study.validation.enumeration;

import lombok.Getter;

public enum ValidCode {

    NOT_BLANK("ERR_01")
    ,NOT_NULL("ERR_02")
    ,PATTERN("ERR_03")
    ,MAX("ERR_04")
    ,EMAIL("ERR_05");

    @Getter
    private String code;

    ValidCode(String code){
        this.code = code;
    }
}
