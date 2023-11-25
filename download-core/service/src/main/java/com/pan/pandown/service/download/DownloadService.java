package com.pan.pandown.service.download;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pan.pandown.api.RequestService;
import com.pan.pandown.api.model.BaiduApiErrorNo;
import com.pan.pandown.dao.entity.DbtableCommonAccount;
import com.pan.pandown.dao.entity.DbtableSvip;
import com.pan.pandown.service.IDbtableCommonAccountService;
import com.pan.pandown.service.IDbtableSvipService;
import com.pan.pandown.service.IPandownParseService;
import com.pan.pandown.service.IPandownUserFlowService;
import com.pan.pandown.util.CookieUtils;
import com.pan.pandown.util.DTO.downloadApi.*;
import com.pan.pandown.util.exception.AccountException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DownloadService {

    @Autowired
    private RequestService requestService;

    @Autowired
    private IDbtableSvipService dbtableSvipService;

    @Autowired
    private IDbtableCommonAccountService dbtableCommonAccountService;

    @Autowired
    private IPandownUserFlowService pandownUserFlowService;

    @Autowired
    private IPandownParseService pandownParseService;

    @Autowired
    private CheckHelper checkHelper;

    /**
     *  以文件树列出文件夹下所有文件，包含文件夹下面的子文件夹
     * @param surl 分享surl
     * @param pwd 分享验证码
     * @param dir 解析目录
     * @param saveList 存储列表
     * @return 存储列表
     */
    public List listFileTree(String surl,String pwd,String dir, List saveList){
        if (saveList == null) saveList = new ArrayList();
        List<Map> dataList = (List)listDir(surl, pwd, dir, 1).get("list");

        for (int i = 0; i < dataList.size(); i++) {
            Map file = dataList.get(i);
            if (Integer.parseInt(file.getOrDefault("isdir",0).toString()) == 1){
                String path = (String) file.get("path");
                List list = listFileTree(surl, pwd, path, new ArrayList<>());
                file.put("children",list);
            }
            saveList.add(file);
        }
        return saveList;
    }

    /**
     * 列出文件夹下所有文件，包含子文件夹下的文件
     * @param surl
     * @param pwd
     * @param dir
     * @param saveList
     * @return
     */
    public List listFiles(String surl,String pwd,String dir, List saveList){
        if (saveList == null) saveList = new ArrayList();
        List<Map> dataList = (List)listDir(surl, pwd, dir, 1).get("list");

        for (int i = 0; i < dataList.size(); i++) {
            Map file = dataList.get(i);
            if (Integer.parseInt(file.getOrDefault("isdir",0).toString()) == 1){
                String path = (String) file.get("path");
                List list = listFileTree(surl, pwd, path, new ArrayList<>());
                saveList.addAll(list);
            }else{
                saveList.add(file);
            }
        }
        return saveList;
    }

    /**
     * 列举分享文件夹下面的文件夹和文件(比如：打开文件夹)
     * @param surl 分享surl
     * @param pwd 分享验证码
     * @param dir 解析目录
     * @param page 请求页码
     * @return
     */
    public Map listDir(String surl,String pwd,String dir,Integer page){

        DbtableCommonAccount account = dbtableCommonAccountService.getNextCommonAccount();
        //检测账号cookie
        Map<String, String> cookie = checkHelper.checkBaiduAccountCookie(account);
        //请求解析目录
        ResponseEntity<Map> responseEntity = requestService.requestFileList(surl, pwd, dir, page,cookie.get("BDUSS"));
        //检测响应
        try {
            checkHelper.checkBaiduResponse(responseEntity);
        } catch (AccountException e) {
            dbtableCommonAccountService.freezeCommonAccount(account.getId());
            throw new RuntimeException(e);
        }

        //返回
        return (Map) responseEntity.getBody().getOrDefault("data",null);

    }




    public SignAndTimeDTO getSignAndTime(String surl){
        DbtableCommonAccount account = dbtableCommonAccountService.getNextCommonAccount();
        if (account == null) throw new RuntimeException("已无可用的百度云普通账号");
        //check account cookie
        Map<String, String> cookie = checkHelper.checkBaiduAccountCookie(account);
        //request sign and time
        ResponseEntity<Map> responseEntity = requestService.requestSignAndTime(surl,cookie.get("BAIDUID"));
        //检测响应
        try {
            checkHelper.checkBaiduResponse(responseEntity);
        } catch (AccountException e) {
            dbtableCommonAccountService.freezeCommonAccount(account.getId());
            throw new RuntimeException(e);
        }

        //mapper 转换
        ObjectMapper objectMapper = new ObjectMapper();
        Object data = responseEntity.getBody().get("data");
        SignAndTimeDTO signAndTimeDTO = objectMapper.convertValue(data, SignAndTimeDTO.class);

        return signAndTimeDTO;
    }

    /**
     * 下载链接获取
     * @param getDlinkDTO
     * @return
     */
    public List<ShareFileDTO> getDlink(GetDlinkDTO getDlinkDTO){
        DbtableCommonAccount account = dbtableCommonAccountService.getNextCommonAccount();
        //check account cookie
        checkHelper.checkBaiduAccountCookie(account);
        //request
        ResponseEntity<Map> responseEntity = requestService.requestDlink(getDlinkDTO,account.getCookie());
        //检测响应
        try {
            checkHelper.checkBaiduResponse(responseEntity);
        } catch (AccountException e) {
            dbtableCommonAccountService.freezeCommonAccount(account.getId());
            throw new RuntimeException(e);
        }

        //json转Java对象
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        List<Object> list = (List<Object>)responseEntity.getBody().getOrDefault("list", null);
        //转化
        List<ShareFileDTO> collect = list.stream()
                .map(o -> objectMapper.convertValue(o, ShareFileDTO.class))
                .collect(Collectors.toList());

        return collect;
    }

    /**
     * svip下载链接获取
     * @param shareFileDTO
     * @return
     */
    public String fetchSvipDlink(ShareFileDTO shareFileDTO,Long id){
        //消耗流量
        boolean b = pandownUserFlowService.consumeUserFlow(id, shareFileDTO.getSize());
        if (!b) throw new RuntimeException("流量不足");

        DbtableSvip nextSvip = dbtableSvipService.getNextSvip();
        ResponseEntity<Map> responseEntity = requestService.requestSvipDlink(shareFileDTO.getDlink(),nextSvip.getSvipBduss());

        try {
            checkHelper.checkBaiduSvipResponseHeader(responseEntity);
        }catch (RuntimeException e){
            ResponseEntity<Map> svipStatus = requestService.requestSvipDlink(shareFileDTO.getDlink(), nextSvip.getSvipBduss(), HttpMethod.GET);
            try {
                checkHelper.checkBaiduSvipResponseBody(svipStatus);
            } catch (AccountException ex) {
                dbtableSvipService.freezeSvip(nextSvip.getId());
                throw new RuntimeException(ex.getMessage());
            }
            throw e;
        }


        List<String> location = responseEntity.getHeaders().get("Location");
        String link = location.get(0);
        shareFileDTO.setSvipDlink(link);
        pandownParseService.saveByShareFileDTO(shareFileDTO,id,nextSvip.getId());
        return link;
    }

    /**
     * svip下载链接获取
     * @param getSvipDlinkDTO
     * @param id 解析用户的id
     * @return
     */
    public ShareFileDTO getSvipDlink(GetSvipDlinkDTO getSvipDlinkDTO,Long id){
        String sign = getSvipDlinkDTO.getSign();
        Long timestamp = getSvipDlinkDTO.getTimestamp();
        String seckey = getSvipDlinkDTO.getSeckey();
        String shareid = getSvipDlinkDTO.getShareid();
        String uk = getSvipDlinkDTO.getUk();

        ShareFileDTO shareFile = getSvipDlinkDTO.getShareFile();
        //如果普通连接为空，去提取
        if (StringUtils.isBlank(shareFile.getDlink())){
            ArrayList fileIdList = new ArrayList();
            fileIdList.add(shareFile.getFs_id());
            GetDlinkDTO getDlinkDTO = new GetDlinkDTO();
            getDlinkDTO.setFsIdList(fileIdList);
            getDlinkDTO.setUk(uk);
            getDlinkDTO.setSeckey(seckey);
            getDlinkDTO.setSign(sign);
            getDlinkDTO.setTimestamp(timestamp);
            getDlinkDTO.setShareid(shareid);

            //获取普通连接
            List<ShareFileDTO> shareFileDTOList = getDlink(getDlinkDTO);
            shareFile = shareFileDTOList.get(0);
        }

        //获取SVIP连接
        String link = fetchSvipDlink(shareFile,id);
        //填充SvipDlink字段
        shareFile.setSvipDlink(link);

        return shareFile;
    }

    public List<ShareFileDTO> getAllSvipdLink(ListAllSvipDlinkDTO listAllSvipDlinkDTO,Long id){
        String surl = listAllSvipDlinkDTO.getSurl();
        String pwd = listAllSvipDlinkDTO.getPwd();
        String seckey = listAllSvipDlinkDTO.getSeckey();
        String uk = listAllSvipDlinkDTO.getUk();
        String shareid = listAllSvipDlinkDTO.getShareid();
        List<ShareFileDTO> shareFileDTOList = listAllSvipDlinkDTO.getShareFileList();

        //解析文件id列表
        ArrayList<String> fileidList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //解析所有文件
        for (int i = 0;i<shareFileDTOList.size();i++){
            ShareFileDTO shareFile = shareFileDTOList.get(i);
            int isdir = shareFile.getIsdir();
            //如果是目录，解析目录，提取目录下所有的fs_id
            if(isdir==1){
                List<Map> list = listFiles(surl, pwd, shareFile.getPath(), new ArrayList());
                List collect = list.stream()
                        .map(o -> o.get("fs_id").toString())
                        .collect(Collectors.toList());
                fileidList.addAll(collect);
            }else{
                fileidList.add(shareFile.getFs_id());
            }
        }
        //获取签名和时间搓
        SignAndTimeDTO signAndTime = getSignAndTime(surl);

        GetDlinkDTO getDlinkDTO = new GetDlinkDTO();
        getDlinkDTO.setSeckey(seckey);
        getDlinkDTO.setUk(uk);
        getDlinkDTO.setShareid(shareid);
        getDlinkDTO.setSign(signAndTime.getSign());
        getDlinkDTO.setTimestamp(signAndTime.getTimestamp());
        getDlinkDTO.setFsIdList(fileidList);
        List<ShareFileDTO> list = getDlink(getDlinkDTO); //获取所有文件的普通下载对象

        ArrayList<ShareFileDTO> result = new ArrayList<>();
        //用dlink换svipDlink
        for (int i = 0; i < list.size(); i++) {
            ShareFileDTO shareFileDTO = list.get(i);
            String link = fetchSvipDlink(shareFileDTO,id);
            shareFileDTO.setSvipDlink(link);
            result.add(shareFileDTO);
        }

        return result;
    }











}
