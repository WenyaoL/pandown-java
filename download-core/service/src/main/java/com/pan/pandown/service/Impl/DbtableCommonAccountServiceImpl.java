package com.pan.pandown.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pan.pandown.api.RequestService;
import com.pan.pandown.dao.entity.DbtableCommonAccount;
import com.pan.pandown.dao.mapper.DbtableCommonAccountMapper;
import com.pan.pandown.service.IDbtableCommonAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pan.pandown.service.download.CheckHelper;
import com.pan.pandown.util.CookieUtils;
import com.pan.pandown.util.DTO.pandownCommonAccountApi.CommonAccountNumDTO;
import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import com.pan.pandown.service.common.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yalier
 * @since 2023-05-16
 */
@Slf4j
@Service
public class DbtableCommonAccountServiceImpl extends ServiceImpl<DbtableCommonAccountMapper, DbtableCommonAccount> implements IDbtableCommonAccountService {

    @Autowired
    private RedisService redisService;

    @Resource
    private RequestService requestService;

    @Resource
    private SnowflakeGenerator snowflakeGenerator;

    @Autowired
    private CheckHelper checkHelper;

    private final String PANDOWN_COMMON_ACCOUNT_IDX = "Pandown_common_account:for_idx";
    private final String PANDOWN_COMMON_ACCOUNT_LIST = "Pandown_common_account:account_list";


    @Override
    public CommonAccountNumDTO getAccountNumDetail() {
        int count = count();
        Long accountNumByState = baseMapper.getAccountNumByState(1);

        CommonAccountNumDTO commonAccountNumDTO = new CommonAccountNumDTO(count, accountNumByState);

        return commonAccountNumDTO;
    }

    @Override
    public List<DbtableCommonAccount> listAccountDetail() {
        List<DbtableCommonAccount> list = list();
        return list;
    }

    @Override
    public boolean deleteAccountDetail(Long id,String name) {
        QueryWrapper<DbtableCommonAccount> queryWrapper = new QueryWrapper<DbtableCommonAccount>()
                .eq("id", id)
                .eq("name", name);
        return remove(queryWrapper);
    }

    @Override
    public boolean updateAccountDetail(Long id,String name,String cookie) {
        //提取cookie
        Map<String, String> cookieMap = CookieUtils.strToCookieMap(cookie);
        String bduss = cookieMap.get("BDUSS");
        String stoken = cookieMap.get("STOKEN");

        //查询账号状态
        ResponseEntity<Map> responseEntity = requestService.requestAccountState(bduss, stoken);

        if (!checkHelper.isSuccessBaiduResponse(responseEntity)) throw new RuntimeException("跟新账号异常");



        Map result = (Map) responseEntity.getBody().get("result");

        String username = result.get("username").toString();
        String loginstate = result.get("loginstate").toString();

        //判断登录状态，未登录不给予通过
        if (loginstate.equals("0")) return false;

        boolean update = update()
                .set("cookie", cookie)
                .eq("id", id)
                .eq("name", name)
                .update();

        return update;

    }

    @Override
    public DbtableCommonAccount addAccountDetail(String cookie) {
        //提取cookie
        Map<String, String> cookieMap = CookieUtils.strToCookieMap(cookie);
        String bduss = cookieMap.get("BDUSS");
        String stoken = cookieMap.get("STOKEN");

        //查询账号状态
        ResponseEntity<Map> responseEntity = requestService.requestAccountState(bduss, stoken);
        if (!checkHelper.isSuccessBaiduResponse(responseEntity)) throw new RuntimeException("添加失败，账号异常");
        Map result = (Map) responseEntity.getBody().get("result");

        String username = result.get("username").toString();
        String loginstate = result.get("loginstate").toString();

        //判断登录状态，未登录不给予通过
        if (loginstate.equals("0")) return null;

        //生成雪花
        long nextId = snowflakeGenerator.nextId();

        //注入信息
        DbtableCommonAccount dbtableCommonAccount = new DbtableCommonAccount();
        dbtableCommonAccount.setId(nextId);
        dbtableCommonAccount.setName(username);
        dbtableCommonAccount.setCookie(cookie);
        dbtableCommonAccount.setCreateTime(LocalDateTime.now());
        dbtableCommonAccount.setUpdateTime(LocalDateTime.now());
        dbtableCommonAccount.setState(1);

        boolean save = save(dbtableCommonAccount);
        if (!save) throw new RuntimeException("添加失败");
        return dbtableCommonAccount;
    }

    @Override
    public String getNextCommonAccountCookie() {
        DbtableCommonAccount account = getNextCommonAccount();
        return account.getCookie();
    }

    @Override
    public List<DbtableCommonAccount> listAvailableAccount() {
        List<DbtableCommonAccount> accountList = query().eq("state", 1).list();
        log.warn(accountList.toString());
        log.warn("accountList:{}",accountList);
        return accountList;
    }

    public DbtableCommonAccount getNextCommonAccount(){
        boolean b = redisService.hasKey(PANDOWN_COMMON_ACCOUNT_LIST);
        if (!b){
            List<DbtableCommonAccount> list = listAvailableAccount();
            if (list.size()==0) return null;


            redisService.lPush(PANDOWN_COMMON_ACCOUNT_LIST, list);
            redisService.set(PANDOWN_COMMON_ACCOUNT_IDX,0L);
        }

        long incr = redisService.incr(PANDOWN_COMMON_ACCOUNT_IDX, 1);
        long l = redisService.lGetListSize(PANDOWN_COMMON_ACCOUNT_LIST);

        DbtableCommonAccount account = (DbtableCommonAccount)redisService.lGetIndex(PANDOWN_COMMON_ACCOUNT_LIST, incr % l);
        return account;
    }


    @Override
    public boolean freezeCommonAccount(Long id) {
        boolean update = update().eq("id", id)
                .set("state", 0)
                .update();
        List<DbtableCommonAccount> list = listAvailableAccount();

        redisService.del(PANDOWN_COMMON_ACCOUNT_LIST,PANDOWN_COMMON_ACCOUNT_IDX);
        redisService.set(PANDOWN_COMMON_ACCOUNT_IDX,0L);
        if (list == null || list.size()==0) throw new RuntimeException("已无可用的百度云普通账号");
        redisService.lPush(PANDOWN_COMMON_ACCOUNT_LIST, list);

        return update;
    }
}
