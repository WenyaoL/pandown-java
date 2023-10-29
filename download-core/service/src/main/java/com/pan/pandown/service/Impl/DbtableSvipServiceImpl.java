package com.pan.pandown.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pan.pandown.api.RequestService;
import com.pan.pandown.dao.entity.DbtableSvip;
import com.pan.pandown.dao.mapper.DbtableSvipMapper;
import com.pan.pandown.service.IDbtableSvipService;
import com.pan.pandown.service.download.CheckHelper;
import com.pan.pandown.util.DTO.pandownSvipAccountApi.SvipAccountNumDTO;
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
 * @author yalier(wenyao)
 * @since 2023-04-17
 */
@Slf4j
@Service
public class DbtableSvipServiceImpl extends ServiceImpl<DbtableSvipMapper, DbtableSvip> implements IDbtableSvipService {


    @Resource
    private DbtableSvipMapper dbtableSvipMapper;

    @Resource
    private SnowflakeGenerator snowflakeGenerator;

    @Resource
    private RequestService requestService;

    @Resource
    private RedisService redisService;

    @Autowired
    private CheckHelper checkHelper;

    private final String PANDOWN_SVIP = "Pandown_svip:";
    private final String PANDOWN_SVIP_IDX = "for_idx";
    private final String PANDOWN_SVIP_LIST = "svip_list";

    @Override
    public SvipAccountNumDTO getSvipNumDetail() {
        int count = count();
        Long svipNumByState = dbtableSvipMapper.getSvipNumByState("1");

        SvipAccountNumDTO svipAccountNumDTO = new SvipAccountNumDTO(count, svipNumByState);

        return svipAccountNumDTO;
    }

    @Override
    public List<DbtableSvip> getSvipDetail() {
        List<DbtableSvip> svipDetail = dbtableSvipMapper.getSvipDetail();
        return svipDetail;
    }

    @Override
    public boolean deleteSvipDetail(Long id) {
        return removeById(id);
    }

    @Override
    public boolean updateSvipDetail(Long id,String bduss,String stoken) {

        ResponseEntity<Map> responseEntity = requestService.requestAccountState(bduss, stoken);
        if (!checkHelper.isSuccessBaiduResponse(responseEntity)) throw new RuntimeException("更新的账号存在异常");

        Map result = (Map)responseEntity.getBody().get("result");
        String username = result.get("username").toString();
        String loginstate = result.get("loginstate").toString();
        Integer is_vip = Integer.parseInt(result.get("is_vip").toString());
        Integer is_svip = Integer.parseInt(result.get("is_svip").toString());
        Integer is_evip = Integer.parseInt(result.get("is_evip").toString());

        //判断登录状态
        if (loginstate.equals("0")) return false;

        //判断会员类型
        int vip_type = 0;
        if (is_vip.equals(1)) vip_type = 1;
        if (is_svip.equals(1)) vip_type = 2;
        if (is_evip.equals(1)) vip_type = 3;

        if (vip_type == 0) return false;

        boolean update = update().eq("id", id)
                .set("svip_bduss", bduss)
                .set("svip_stoken", stoken)
                .set("name",username)
                .set("vip_type",vip_type)
                .set("update_time",LocalDateTime.now())
                .update();
        return update;

    }

    @Override
    public DbtableSvip addSvipDetail(String bduss,String stoken) {

        ResponseEntity<Map> responseEntity = requestService.requestAccountState(bduss, stoken);
        //检测响应
        if (!checkHelper.isSuccessBaiduResponse(responseEntity)) throw new RuntimeException("添加的账号存在异常");

        Map result = (Map)responseEntity.getBody().get("result");
        String username = result.get("username").toString();
        String loginstate = result.get("loginstate").toString();
        Integer is_vip = Integer.parseInt(result.get("is_vip").toString());
        Integer is_svip = Integer.parseInt(result.get("is_svip").toString());
        Integer is_evip = Integer.parseInt(result.get("is_evip").toString());;

        //判断登录状态
        if (loginstate.equals("0")) return null;
        //判断会员类型
        int vip_type = 0;
        if (is_vip.equals(1)) vip_type = 1;
        if (is_svip.equals(1)) vip_type = 2;
        if (is_evip.equals(1)) vip_type = 3;
        if (vip_type == 0) return null;


        DbtableSvip dbtableSvip = new DbtableSvip();
        //生成雪花
        long nextId = snowflakeGenerator.nextId();

        //注入用户名和状态等信息
        dbtableSvip.setId(nextId);
        dbtableSvip.setName(username);
        dbtableSvip.setSvipBduss(bduss);
        dbtableSvip.setSvipStoken(stoken);
        dbtableSvip.setState(1);
        dbtableSvip.setVipType(vip_type);
        dbtableSvip.setCreateTime(LocalDateTime.now());
        dbtableSvip.setUpdateTime(LocalDateTime.now());

        if (!save(dbtableSvip))throw new RuntimeException("添加失败");
        return dbtableSvip;

    }

    @Override
    public List<DbtableSvip> listAvailableSvip() {
        List<DbtableSvip> svipList = query().eq("state", 1).list();
        return svipList;
    }

    @Override
    public DbtableSvip getNextSvip() {
        if(!redisService.hasKey(PANDOWN_SVIP+PANDOWN_SVIP_LIST)){
            List<DbtableSvip> list = listAvailableSvip();
            if (list.size()==0) return null;
            redisService.multi();
            redisService.del(PANDOWN_SVIP + PANDOWN_SVIP_IDX,PANDOWN_SVIP + PANDOWN_SVIP_LIST);
            redisService.set(PANDOWN_SVIP + PANDOWN_SVIP_IDX,0);
            redisService.lPush(PANDOWN_SVIP + PANDOWN_SVIP_LIST, list);
            redisService.exec();
        }

        long incr = redisService.incr(PANDOWN_SVIP + PANDOWN_SVIP_IDX,1);
        long l = redisService.lGetListSize(PANDOWN_SVIP + PANDOWN_SVIP_LIST);
        DbtableSvip dbtableSvip = (DbtableSvip) redisService.lGetIndex(PANDOWN_SVIP + PANDOWN_SVIP_LIST,incr%l);
        return dbtableSvip;
    }

    @Override
    public boolean freezeSvip(Long id) {
        boolean update = update().eq("id", id)
                .set("state", 0)
                .update();

        List<DbtableSvip> list = listAvailableSvip();
        redisService.multi();
        redisService.del(PANDOWN_SVIP + PANDOWN_SVIP_IDX,PANDOWN_SVIP + PANDOWN_SVIP_LIST);
        redisService.set(PANDOWN_SVIP + PANDOWN_SVIP_IDX,0);
        redisService.lPush(PANDOWN_SVIP + PANDOWN_SVIP_LIST, list);
        redisService.exec();
        return update;
    }





}
