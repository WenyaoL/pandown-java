package com.pan.pandown.api.model;


/**
 * BAIDU API 的错误消息枚举类
 */
public enum BaiduApiErrorNo {

    NOT_KNOWN_ERROR(Integer.MAX_VALUE,"未知错误:"),

    ERROR_ACCOUNT_COOKIE(9019,"账号cookie信息失效"),
    ERROR_ACCOUNT_NOLOGIN(-6,"请检查云盘普通账号是否正常登录"),
    ERROR_ACCOUNT_NOT_Available(9013,"账号被限制，请检查普通账号状态"),
    ERROR_ACCOUNT_D_Available(8001,"账号可能被限制，请检查普通账号状态"),

    ERROR_LINK_INVALIDATED(-130,"链接失效"),
    ERROR_Captcha(-20,"触发验证码,请等待一段时间,再返回首页重新解析"),
    ERROR_FILE_NOTFOUND(-9,"文件不存在,重新解析"),
    ERROR_FILE(-1,"您下载的内容中包含违规信息"),
    SUCCESS(0,"success");



    private int code;
    private String msg;

    BaiduApiErrorNo(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg(){
        return msg;
    }

    /**
     * 属于账号问题，如账号被限速，账号未登录等
     * @param baiduApiErrorNo
     * @return
     */
    public static boolean isAccountError(BaiduApiErrorNo baiduApiErrorNo){

        switch (baiduApiErrorNo){
            case ERROR_ACCOUNT_NOLOGIN:
            case ERROR_ACCOUNT_D_Available:
            case ERROR_ACCOUNT_NOT_Available:
            case ERROR_ACCOUNT_COOKIE:return true;
            default:return false;
        }

    }

    /**
     * 属于链接错误，如触发验证码，链接失效等
     * @param baiduApiErrorNo
     * @return
     */
    public static boolean isLinkError(BaiduApiErrorNo baiduApiErrorNo){

        switch (baiduApiErrorNo){
            case ERROR_Captcha:
            case ERROR_FILE_NOTFOUND:
            case ERROR_FILE:
            case ERROR_LINK_INVALIDATED:return true;
            default:return false;
        }

    }

    public static boolean isUnknownError(BaiduApiErrorNo baiduApiErrorNo){
        return BaiduApiErrorNo.NOT_KNOWN_ERROR.equals(baiduApiErrorNo);
    }

    public static boolean isSuccess(BaiduApiErrorNo baiduApiErrorNo){

        switch (baiduApiErrorNo){
            case SUCCESS:return true;
            default:return false;
        }
    }

    public static BaiduApiErrorNo toBaiduApiError(String code){
        return toBaiduApiError(Integer.valueOf(code));
    }
    public static BaiduApiErrorNo toBaiduApiError(int code){
        switch (code){
            case -130: return BaiduApiErrorNo.ERROR_LINK_INVALIDATED;
            case -20: return BaiduApiErrorNo.ERROR_Captcha;
            case -9:return BaiduApiErrorNo.ERROR_FILE_NOTFOUND;
            case -6:return BaiduApiErrorNo.ERROR_ACCOUNT_NOLOGIN;
            case -1:return BaiduApiErrorNo.ERROR_FILE;
            case 0: return BaiduApiErrorNo.SUCCESS;
            case 8001: return BaiduApiErrorNo.ERROR_ACCOUNT_D_Available;
            case 9013: return BaiduApiErrorNo.ERROR_ACCOUNT_NOT_Available;
            case 9019: return BaiduApiErrorNo.ERROR_ACCOUNT_COOKIE;
            default: return BaiduApiErrorNo.NOT_KNOWN_ERROR;
        }
    }
}
