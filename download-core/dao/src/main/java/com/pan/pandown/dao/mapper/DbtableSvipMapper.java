package com.pan.pandown.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pan.pandown.dao.entity.DbtableSvip;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yalier(wenyao)
 * @since 2023-04-17
 */
public interface DbtableSvipMapper extends BaseMapper<DbtableSvip> {


    @Select("SELECT COUNT(*) FROM dbtable_svip WHERE state = #{state}")
    Long getSvipNumByState(String state);

    List<DbtableSvip> getSvipDetail();
}
