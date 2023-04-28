package com.pan.pandown.util.baseResp;

import com.pan.pandown.util.constants.ResponseCode;
/**
 * Description: 返回前台成功返回类
 */

public class SuccessResponse extends BaseResponse {

    /**
     * 返回前台成功标识
     */
    private static final String CODE_SUCCESS = ResponseCode.SUCCESS.getCode();

    public SuccessResponse (){
        super();
    }

    public SuccessResponse (Object data){
        super();
        put("msg",ResponseCode.SUCCESS.getName());
        put("data",data);
    }

    @Override
    protected void setCode() {
        this.put(KEY_CODE , CODE_SUCCESS );
    }
}
