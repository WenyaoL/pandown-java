package com.pan.pandown.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pan.pandown.util.DTO.pandownAdminApi.AddUserDetailDTO;
import com.pan.pandown.util.DTO.pandownAdminApi.UpdateUserDetailDTO;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-24
 */
public interface IPandownAdminService {

    IPage getUserInfoByPage(int pageNum, int pageSize);

    boolean updateUserDetail(UpdateUserDetailDTO updateUserDetailDTO);

    boolean addUserDetail(AddUserDetailDTO addUserDetailDTO);
}
