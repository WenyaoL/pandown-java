package com.pan.pandown.service.Impl;

import com.pan.pandown.dao.entity.PandownParse;
import com.pan.pandown.dao.mapper.PandownParseMapper;
import com.pan.pandown.service.IPandownParseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pan.pandown.util.DTO.downloadApi.ShareFileDTO;
import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yalier
 * @since 2023-05-26
 */
@Service
public class PandownParseServiceImpl extends ServiceImpl<PandownParseMapper, PandownParse> implements IPandownParseService {



    @Override
    public boolean saveByShareFileDTO(ShareFileDTO shareFileDTO,Long userId,Long svipAccountId) {

        PandownParse pandownParse = new PandownParse();
        pandownParse.setUserId(userId);
        pandownParse.setFilename(shareFileDTO.getServer_filename());
        pandownParse.setMd5(shareFileDTO.getMd5());
        pandownParse.setPath(shareFileDTO.getPath());
        pandownParse.setReallink(shareFileDTO.getSvipDlink());
        pandownParse.setSize(shareFileDTO.getSize());
        pandownParse.setParseAccountId(svipAccountId);
        pandownParse.setCreateTime(LocalDateTime.now());
        return save(pandownParse);
    }
}
