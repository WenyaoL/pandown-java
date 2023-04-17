package com.pan.pandown.util.baseResp;


import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: 前端与后端交互的基础类
 */

public abstract class BaseResponse extends HashMap<Object, Object> {

    private static final long serialVersionUID = 6431763631675851262L;

    public BaseResponse() {
        super();
        setCode();
    }

    public BaseResponse(Map<Object, Object> map) {
        super(map);
        setCode();
    }

    /**
     * 用于返回请求失败时用异常信息生成返回类
     * @param e
     */
    public  BaseResponse(Exception e){ }

    /**
     * 用于返回成功请求，直接用数据生成返回类
     * @param data
     */
    public  BaseResponse( Object data ){ }


    /**
     * 返回前台数据
     */
    protected static final String KEY_DATA = "data";

    /**
     * 返回前台成功失败标志
     */
    protected static final String KEY_CODE = "code";

    /**
     * 返回消息
     */
    protected static final String KEY_MSG = "msg";

    /**
     * 返回消息的详细信息
     */
    protected static final String KEY_MSG_DETAIL = "msgDetail";

    /**
     * 设置默认代码
     * */
    protected abstract void setCode();


    /**
     * 设置返回前台消息
     * */
    public BaseResponse setMsg(String msg) {
        put(KEY_MSG, msg);
        return this;
    }

    /**
     * 获取返回前台消息
     * */
    public String getMsg() {
        if (StringUtils.isEmpty(get(KEY_MSG).toString())) {
            return null;
        }
        return get(KEY_MSG).toString();
    }

    /**
     * 获取返回前台消息
     * */
    public Integer getCode() {
        if (StringUtils.isEmpty(get(KEY_CODE).toString())) {
            return null;
        }
        return Integer.valueOf(get(KEY_CODE).toString());
    }

    /**
     * 设置返回前台数据对象
     * */
    public BaseResponse setData(Object dataObject) {
        put(KEY_DATA, dataObject);
        return this;
    }

    /**
     * 设置返回前台数据对象
     * */
    public Object getData() {
        return get(KEY_DATA);
    }

    /**
     * 返回消息的详细信息
     * @return
     */
    public String getMsgDetail() {
        if (StringUtils.isEmpty(get(KEY_MSG_DETAIL).toString())) {
            return null;
        }
        return get(KEY_MSG_DETAIL).toString();
    }

    /**
     * 详细信息
     * @param msgDetail
     * @return
     */
    public BaseResponse setMsgDetail(String msgDetail) {
        put(KEY_MSG_DETAIL, msgDetail);
        return this;
    }


}
