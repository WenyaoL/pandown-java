package com.pan.pandown.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pan.pandown.api.RequestService;
import com.pan.pandown.dao.entity.DbtableSvip;
import com.pan.pandown.dao.mapper.DbtableSvipMapper;
import com.pan.pandown.service.IDbtableSvipService;
import com.pan.pandown.service.download.DownloadService;
import com.pan.pandown.util.DTO.dbtableSvipApi.AddSvipDetailDTO;
import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import com.pan.pandown.util.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    private final String PANDOWN_SVIP = "Pandown_svip:";
    private final String PANDOWN_SVIP_IDX = "for_idx";
    private final String PANDOWN_SVIP_LIST = "svip_list";

    @Override
    public Map getSvipNumDetail() {
        int count = count();
        Long svipNumByState = dbtableSvipMapper.getSvipNumByState("1");
        HashMap<String, Object> map = new HashMap<String, Object>() {{
            put("accountNum", count);
            put("availableAccountNum",svipNumByState);
        }};
        return map;
    }

    @Override
    public List getSvipDetail() {
        List svipDetail = dbtableSvipMapper.getSvipDetail();
        return svipDetail;
    }

    @Override
    public boolean deleteSvipDetail(DbtableSvip dbtableSvip) {
        boolean b = removeById(dbtableSvip.getId());
        return b;
    }

    @Override
    public boolean updateSvipDetail(DbtableSvip dbtableSvip) {

        ResponseEntity<Map> responseEntity = requestService.requestAccountState(dbtableSvip.getSvipBduss(), dbtableSvip.getSvipStoken());

        if (responseEntity.getStatusCode().is2xxSuccessful()){
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

            boolean update = update().eq("id", dbtableSvip.getId())
                    .set("svip_bduss", dbtableSvip.getSvipBduss())
                    .set("svip_stoken", dbtableSvip.getSvipStoken())
                    .set("name",username)
                    .set("vip_type",vip_type)
                    .set("update_time",LocalDateTime.now())
                    .update();
            return update;
        }

        return false;
    }

    @Override
    public DbtableSvip addSvipDetail(DbtableSvip dbtableSvip) {


        ResponseEntity<Map> responseEntity = requestService.requestAccountState(dbtableSvip.getSvipBduss(), dbtableSvip.getSvipStoken());
        if(Objects.isNull(responseEntity)) return null;


        if (responseEntity.getBody().get("errno").toString().equals("0")){
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

            //生成雪花
            long nextId = snowflakeGenerator.nextId();


            //注入用户名和状态等信息
            dbtableSvip.setId(nextId);
            dbtableSvip.setName(username);
            dbtableSvip.setState(1);
            dbtableSvip.setVipType(vip_type);
            dbtableSvip.setCreateTime(LocalDateTime.now());
            dbtableSvip.setUpdateTime(LocalDateTime.now());
            //保存
            boolean save = save(dbtableSvip);
            if (save){
                return dbtableSvip;
            }

            return null;
        }

        return null;
    }

    @Override
    public DbtableSvip getNextSvip() {
        if(!redisService.hasKey(PANDOWN_SVIP+PANDOWN_SVIP_LIST)){
            redisService.set(PANDOWN_SVIP + PANDOWN_SVIP_IDX,0);
            List<DbtableSvip> list = list();
            redisService.lSet(PANDOWN_SVIP + PANDOWN_SVIP_LIST, list);
        }

        long incr = redisService.incr(PANDOWN_SVIP + PANDOWN_SVIP_IDX,1);
        long l = redisService.lGetListSize(PANDOWN_SVIP + PANDOWN_SVIP_LIST);
        DbtableSvip dbtableSvip = (DbtableSvip) redisService.lGetIndex(PANDOWN_SVIP + PANDOWN_SVIP_LIST,incr%l);
        return dbtableSvip;
    }


}
