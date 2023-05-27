package com.pan.pandown.service;

import com.pan.pandown.dao.entity.PandownParse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pan.pandown.util.DTO.downloadApi.ShareFileDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yalier
 * @since 2023-05-26
 */
public interface IPandownParseService extends IService<PandownParse> {

    boolean saveByShareFileDTO(ShareFileDTO shareFileDTO,Long userId,Long svipAccountId);
}
