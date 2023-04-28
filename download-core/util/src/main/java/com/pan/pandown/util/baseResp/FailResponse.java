package com.pan.pandown.util.baseResp;

import com.pan.pandown.util.constants.ResponseCode;
/**
 * Description: 返回前端失败返回信息类
 */

public class FailResponse extends BaseResponse {
    /**
     * 返回前台成功标识
     */
    private static final String CODE_FAIL = ResponseCode.FAIL.getCode();

    public FailResponse(){
        super();
    }

    public FailResponse (Exception e){
        super();
        this.setMsg(e.getMessage());
    }

    public FailResponse (String s){
        super();
        this.setMsg(s);
    }

    public FailResponse (Object obj){
        super();
        this.setData(obj);
    }

    public FailResponse (String s,Object obj){
        super();
        this.setMsg(s);
        this.setData(obj);
    }

    @Override
    protected void setCode() {
        this.put(KEY_CODE , CODE_FAIL );
    }
}
