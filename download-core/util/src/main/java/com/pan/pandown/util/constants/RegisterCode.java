package com.pan.pandown.util.constants;

public enum RegisterCode {

    SUCCESS("成功","200"),
    FAIL("失败","500"),
    FAIL_NAME_REPEAT("重复用户名","501"),
    FAIL_EMAIL_REPEAT("邮箱已被注册过","502");

    private String name ;

    private String code ;

    RegisterCode(String name,String code){
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
