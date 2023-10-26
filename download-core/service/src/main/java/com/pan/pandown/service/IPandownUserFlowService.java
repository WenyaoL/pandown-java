package com.pan.pandown.service;

import com.pan.pandown.dao.entity.PandownUserFlow;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yalier
 * @since 2023-05-21
 */
public interface IPandownUserFlowService extends IService<PandownUserFlow> {

    /**
     * 获取用户流量信息
     * @param id
     * @return
     */
    PandownUserFlow getUserFlowInfo(Long id);

    /**
     * 初始化用户流量信息
     * @param id
     * @return
     */
    PandownUserFlow initUserFlow(Long id);
    PandownUserFlow initUserFlow(Long id,Integer state);
    PandownUserFlow initUserFlow(Long id,Integer state,Long limitFlow);

    /**
     * 消耗用户流量
     * 消耗成功返回true
     * 消耗失败返回false
     * @param id
     * @param flow
     * @return
     */
    boolean consumeUserFlow(Long id,Long flow);

    /**
     * 判断当前用户流量是否可用
     * @return
     */
    boolean isAvailable(Long id);


}
