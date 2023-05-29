package com.pan.pandown.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pan.pandown.dao.entity.PandownUserFlow;
import com.pan.pandown.dao.mapper.PandownUserFlowMapper;
import com.pan.pandown.service.IPandownUserFlowService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yalier
 * @since 2023-05-21
 */
@Service
public class PandownUserFlowServiceImpl extends ServiceImpl<PandownUserFlowMapper, PandownUserFlow> implements IPandownUserFlowService {


    @Value("${pandown.Flow.limit}")
    private Long defaultLimitFlow;

    @Override
    public PandownUserFlow getUserFlowInfo(Long id) {
        PandownUserFlow pandownUserFlow = this.baseMapper.selectById(id);
        return pandownUserFlow;
    }

    @Override
    public PandownUserFlow initUserFlow(Long id) {
        return initUserFlow(id,1);
    }

    @Override
    public PandownUserFlow initUserFlow(Long id,Integer state) {
        return initUserFlow(id,state,defaultLimitFlow);
    }

    @Override
    public PandownUserFlow initUserFlow(Long id,Integer state,Long limitFlow) {
        PandownUserFlow pandownUserFlow = new PandownUserFlow();
        pandownUserFlow.setId(id);
        pandownUserFlow.setState(state);
        pandownUserFlow.setParseNum(0);
        pandownUserFlow.setParseFlow(0L);
        pandownUserFlow.setLimitFlow(limitFlow);
        save(pandownUserFlow);
        return pandownUserFlow;
    }

    @Override
    public boolean consumeUserFlow(Long id,Long flow) {
        PandownUserFlow userFlow = getById(id);
        if (Objects.isNull(userFlow)){
            if (flow>defaultLimitFlow) return false;
            PandownUserFlow pandownUserFlow = new PandownUserFlow();
            pandownUserFlow.setId(id);
            pandownUserFlow.setState(1);
            pandownUserFlow.setParseNum(1);
            pandownUserFlow.setParseFlow(flow);
            pandownUserFlow.setLimitFlow(defaultLimitFlow);
            save(pandownUserFlow);
            return true;
        }

        Long parseFlow = userFlow.getParseFlow();
        Integer parseNum = userFlow.getParseNum();
        Long limitFlow = userFlow.getLimitFlow();
        if(limitFlow - flow < 0){
            return false;
        }

        userFlow.setParseNum(parseNum+1);
        userFlow.setParseFlow(parseFlow + flow);
        userFlow.setLimitFlow(limitFlow - flow);
        return updateById(userFlow);
    }

    @Override
    public boolean isAvailable(Long id) {
        PandownUserFlow userFlowInfo = getUserFlowInfo(id);
        Integer state = userFlowInfo.getState();
        if (state == 1) return true;
        return false;
    }


}
