package com.pan.pandown.dao.mapper;

import com.pan.pandown.dao.entity.DbtableCommonAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yalier
 * @since 2023-05-16
 */
public interface DbtableCommonAccountMapper extends BaseMapper<DbtableCommonAccount> {

    @Select("SELECT COUNT(*) FROM dbtable_common_account WHERE state = #{state}")
    Long getAccountNumByState(int state);
}
