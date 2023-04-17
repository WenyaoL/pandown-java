package com.pan.pandown.util.constants;

public enum ResponseCode {

    SUCCESS("成功","200"), FAIL("失败","500");

    private String name ;

    private String code ;

    ResponseCode(String name, String  code) {
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
