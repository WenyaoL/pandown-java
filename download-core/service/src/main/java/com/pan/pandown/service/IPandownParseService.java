package com.pan.pandown.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pan.pandown.dao.entity.PandownParse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pan.pandown.util.DTO.downloadApi.ShareFileDTO;

import java.util.List;

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

    boolean deleteByIdAndUserId(Integer id,Long userId);
    boolean deleteByIdAndUserId(List<Integer> idList,Long userId);

    List<PandownParse> listByUserId(Long userId);

    IPage<PandownParse> pageByUserId(Long userId,Long pageNum, Long pageSize);

}
