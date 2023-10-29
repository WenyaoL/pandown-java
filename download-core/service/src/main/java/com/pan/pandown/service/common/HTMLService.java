package com.pan.pandown.service.common;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-08
 */
public class HTMLService {

    static public String getCaptchaHtml(Object captcha){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("<!doctype html><html><head><meta charset='UTF-8'><meta name='viewport'content='width=device-width initial-scale=1'><style>h2{font-size:1.75em;line-height:1.225;border-bottom:1px solid#eee}body.typora-export{padding-left:30px;padding-right:30px}body{font-family:\"Open Sans\",\"Clear Sans\",\"Helvetica Neue\",Helvetica,Arial,'Segoe UI Emoji',sans-serif;color:rgb(51,51,51);line-height:1.6}body{margin:0px;padding:0px;height:auto;inset:0px;font-size:1rem;line-height:1.42857;overflow-x:hidden;background:inherit;tab-size:4}#write{max-width:860px;margin:0 auto;padding:30px;padding-bottom:100px}#write{margin:0px auto;height:auto;width:inherit;word-break:normal;overflow-wrap:break-word;position:relative;white-space:normal;overflow-x:visible;padding-top:36px}a{color:#4183C4}a{cursor:pointer}*,::after,::before{box-sizing:border-box}</style></head><body class='typora-export os-windows'><div class='typora-export-content'><div id='write'class=''><h2 id='pandown-java验证码'><span>Pandown-java验证码</span></h2><p><span>[pandown-java]</span><a href=''><span>")
                .append(captcha)
                .append("</span></a><span>用于pandown-java身份认证，3分钟内有效，请勿泄漏和转发。如非本人操作，请忽略此邮箱验证。</span></p><p>&nbsp;</p></div></div></body></html>");
        return stringBuilder.toString();
    }

    static public String getCaptchaHtml_forgetPwd(Object captcha){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("<!doctype html><html><head><meta charset='UTF-8'><meta name='viewport'content='width=device-width initial-scale=1'><style>h2{font-size:1.75em;line-height:1.225;border-bottom:1px solid#eee}body.typora-export{padding-left:30px;padding-right:30px}body{font-family:\"Open Sans\",\"Clear Sans\",\"Helvetica Neue\",Helvetica,Arial,'Segoe UI Emoji',sans-serif;color:rgb(51,51,51);line-height:1.6}body{margin:0px;padding:0px;height:auto;inset:0px;font-size:1rem;line-height:1.42857;overflow-x:hidden;background:inherit;tab-size:4}#write{max-width:860px;margin:0 auto;padding:30px;padding-bottom:100px}#write{margin:0px auto;height:auto;width:inherit;word-break:normal;overflow-wrap:break-word;position:relative;white-space:normal;overflow-x:visible;padding-top:36px}a{color:#4183C4}a{cursor:pointer}*,::after,::before{box-sizing:border-box}</style></head><body class='typora-export os-windows'><div class='typora-export-content'><div id='write'class=''><h2 id='pandown-java验证码'><span>Pandown-java验证码</span></h2><p><span>[pandown-java]</span><a href=''><span>")
                .append(captcha)
                .append("</span></a><span>用于<strong>pandown-java密码重置</strong>，3分钟内有效，请勿泄漏和转发。如非本人操作，请忽略此邮箱验证。</span></p><p>&nbsp;</p></div></div></body></html>");
        return stringBuilder.toString();
    }
}
