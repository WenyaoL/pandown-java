package com.pan.pandown.service.download;

import com.pan.pandown.api.model.BaiduApiErrorNo;
import com.pan.pandown.dao.entity.DbtableCommonAccount;
import com.pan.pandown.dao.entity.DbtableSvip;
import com.pan.pandown.service.IDbtableCommonAccountService;
import com.pan.pandown.util.CookieUtils;
import com.pan.pandown.util.exception.AccountException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class CheckHelper {

    public boolean isSuccessBaiduResponse(BaiduApiErrorNo baiduApiError){
        return BaiduApiErrorNo.isSuccess(baiduApiError);
    }
    public boolean isSuccessBaiduResponse(ResponseEntity<Map> responseEntity){
        if (responseEntity == null) return false;
        //获取状态消息
        String errno = responseEntity.getBody().get("errno").toString();
        BaiduApiErrorNo baiduApiError = BaiduApiErrorNo.toBaiduApiError(errno);
        return isSuccessBaiduResponse(baiduApiError);
    }



    /**
     * 检测普通接口的响应
     * @param responseEntity
     */
    public void checkBaiduResponse(ResponseEntity<Map> responseEntity) throws AccountException {


        //无响应
        if (responseEntity == null) throw new RuntimeException("请求异常");

        //获取状态消息
        String errno = responseEntity.getBody().get("errno").toString();
        BaiduApiErrorNo baiduApiError = BaiduApiErrorNo.toBaiduApiError(errno);

        if (isSuccessBaiduResponse(baiduApiError)) return;

        //账号问题 冻结
        if (BaiduApiErrorNo.isAccountError(baiduApiError)) {
            throw new AccountException(baiduApiError.getMsg());
        }

        //链接问题 抛异常
        if (BaiduApiErrorNo.isLinkError(baiduApiError)) {
            throw new RuntimeException(baiduApiError.getMsg());
        }

        //未知错误 抛异常
        if (BaiduApiErrorNo.isUnknownError(baiduApiError)){
            throw new RuntimeException(baiduApiError.getMsg() + errno);
        }

    }


    /**
     * 检测svip接口的响应
     * @param responseEntity
     * @param account
     */
    public void checkBaiduSvipResponse(ResponseEntity<String> responseEntity, DbtableSvip account) throws RuntimeException{

        List<String> location = responseEntity.getHeaders().get("Location");

        if (Objects.isNull(location) && location.size()==0) throw new RuntimeException("SVIP链接抓取失败");

    }

    public Map<String, String> checkBaiduAccountCookie(DbtableCommonAccount account){
        String cookie = account.getCookie();
        Map<String, String> cookieMap = CookieUtils.strToCookieMap(cookie);

        String bduss = cookieMap.get("BDUSS");
        if (StringUtils.isBlank(bduss)) throw new RuntimeException("账号:"+account.getId()+"cookie异常,缺失BDUSS");

        String stoken = cookieMap.get("STOKEN");
        if (StringUtils.isBlank(stoken)) throw new RuntimeException("账号:"+account.getId()+"cookie异常,缺失STOKEN");

        String baiduid = cookieMap.get("BAIDUID");
        if (StringUtils.isBlank(baiduid)) {
            log.error(cookieMap.toString());
            throw new RuntimeException("账号:"+account.getId()+"cookie异常,缺失BAIDUID");
        }


        return cookieMap;
    }



}
