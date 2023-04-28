package com.pan.pandown.util.baseResp;

/**
 * @author wenyao
 */
public enum ResponseCode {

    /**
     * 成功
     */
    SUCCESS(200),

    /**
     * 失败
     */
    FAIL(500);



    public int code;


    ResponseCode(int code) {
        this.code = code;

    }
}
