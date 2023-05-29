package com.pan.pandown.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pan.pandown.dao.entity.PandownParse;
import com.pan.pandown.dao.mapper.PandownParseMapper;
import com.pan.pandown.service.IPandownParseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pan.pandown.util.DTO.downloadApi.ShareFileDTO;
import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public boolean deleteByIdAndUserId(Integer id, Long userId) {
        LambdaQueryWrapper<PandownParse> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PandownParse::getId,id)
                .eq(PandownParse::getUserId,userId);
        return remove(lambdaQueryWrapper);
    }

    @Override
    public boolean deleteByIdAndUserId(List<Integer> idList, Long userId) {
        LambdaQueryWrapper<PandownParse> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(PandownParse::getId,idList)
                .eq(PandownParse::getUserId,userId);
        return remove(lambdaQueryWrapper);
    }

    @Override
    public List<PandownParse> listByUserId(Long userId) {
        List<PandownParse> list = lambdaQuery().eq(PandownParse::getUserId, userId).list();
        return list;
    }

    @Override
    public IPage<PandownParse> pageByUserId(Long userId, Long pageNum, Long pageSize) {
        Page<PandownParse> page = new Page<>(pageNum,pageSize);

        IPage<PandownParse> pandownParseIPage = lambdaQuery()
                .eq(PandownParse::getUserId, userId)
                .page(page);

        log.warn(pandownParseIPage.toString());
        return pandownParseIPage;
    }

}
