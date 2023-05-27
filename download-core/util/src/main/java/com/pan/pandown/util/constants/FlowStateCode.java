package com.pan.pandown.util.constants;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-26
 */
public enum FlowStateCode {
    SUCCESS("成功","1"),
    FAIL_FLOW("流量不够或冻结","-1"),
    FAIL("失败","0");

    private String name ;

    private String code ;

    FlowStateCode(String name, String  code) {
        this.name = name;
        this.code = code;
    }

    public String getCode (){
        return this.code;
    }

    public String getName (){
        return  this.name;
    }


    @Override
    public String toString() {
        return "ResponseCode{name="+this.name+",code="+this.code+"}";
    }
}
