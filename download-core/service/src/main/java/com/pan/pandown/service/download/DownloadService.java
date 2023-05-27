package com.pan.pandown.service.download;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pan.pandown.api.RequestService;
import com.pan.pandown.dao.entity.DbtableCommonAccount;
import com.pan.pandown.dao.entity.DbtableSvip;
import com.pan.pandown.service.IDbtableCommonAccountService;
import com.pan.pandown.service.IDbtableSvipService;
import com.pan.pandown.service.IPandownParseService;
import com.pan.pandown.service.IPandownUserFlowService;
import com.pan.pandown.util.DTO.downloadApi.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 返回所有分享信息
     * @param surl 分享surl
     * @param pwd 分享验证码
     * @return
     */
    public Map listShare(String surl,String pwd){
        ResponseEntity<Map> responseEntity = requestService.requestFileList(surl, pwd, "", 1);
        Map data = (Map) responseEntity.getBody().get("data");
        //链接有效检验
        if(Objects.nonNull(data) && Objects.nonNull(data.get("list")) && ((List)data.get("list")).size()>0){
            data.put("list",listFileTree(surl,pwd,"",new ArrayList()));
            return data;
        }

        return null;
    }

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
        String cookieStr = account.getCookie();
        Map<String, String> cookieMap = requestService.strToCookieMap(cookieStr);

        ResponseEntity<Map> responseEntity = requestService.requestFileList(surl, pwd, dir, page,cookieMap.get("BDUSS"));

        //请求失败(请求失败)
        if (!requestService.isSuccessResponse(responseEntity)){
            //冻结
            dbtableCommonAccountService.freezeCommonAccount(account.getId());
            //再次获取
            account = dbtableCommonAccountService.getNextCommonAccount();
            cookieStr = account.getCookie();
            cookieMap = requestService.strToCookieMap(cookieStr);
            responseEntity = requestService.requestFileList(surl, pwd, dir, page,cookieMap.get("BDUSS"));

            //还是失败(使用静态cookie)
            if(!requestService.isSuccessResponse(responseEntity)) responseEntity = requestService.requestFileList(surl, pwd, dir, page);

            //返回
            return (Map) responseEntity.getBody().getOrDefault("data",null);
        }


        return (Map) responseEntity.getBody().getOrDefault("data",null);
    }




    public SignAndTimeDTO getSignAndTime(String surl){
        DbtableCommonAccount account = dbtableCommonAccountService.getNextCommonAccount();
        String cookieStr = account.getCookie();
        Map<String, String> cookieMap = requestService.strToCookieMap(cookieStr);

        ResponseEntity<Map> responseEntity = requestService.requestSignAndTime(surl,cookieMap.get("BAIDUID"));

        //mapper 转换
        ObjectMapper objectMapper = new ObjectMapper();

        if (!requestService.isSuccessResponse(responseEntity)){
            //冻结
            dbtableCommonAccountService.freezeCommonAccount(account.getId());

            //再次获取
            account = dbtableCommonAccountService.getNextCommonAccount();
            cookieStr = account.getCookie();
            cookieMap = requestService.strToCookieMap(cookieStr);
            responseEntity = requestService.requestSignAndTime(surl,cookieMap.get("BAIDUID"));

            //还是失败,使用静态cookie重新请求一次
            if (!requestService.isSuccessResponse(responseEntity)) responseEntity = requestService.requestSignAndTime(surl);

            Object data = responseEntity.getBody().get("data");
            SignAndTimeDTO signAndTimeDTO = objectMapper.convertValue(data, SignAndTimeDTO.class);
            return signAndTimeDTO;
        }

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
        String cookieStr = account.getCookie();
        Map<String, String> cookieMap = requestService.strToCookieMap(cookieStr);
        ResponseEntity<Map> responseEntity = requestService.requestDlink(getDlinkDTO,cookieMap);

        //json转Java对象
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (!requestService.isSuccessResponse(responseEntity)){
            //冻结
            dbtableCommonAccountService.freezeCommonAccount(account.getId());

            //再次获取
            account = dbtableCommonAccountService.getNextCommonAccount();
            cookieStr = account.getCookie();
            cookieMap = requestService.strToCookieMap(cookieStr);
            responseEntity = requestService.requestDlink(getDlinkDTO,cookieMap);

            //还是失败,使用静态cookie重新请求一次
            if (!requestService.isSuccessResponse(responseEntity)) responseEntity = requestService.requestDlink(getDlinkDTO,cookieMap);

            List<Object> list = (List<Object>)responseEntity.getBody().getOrDefault("list", null);

            //转化
            List<ShareFileDTO> collect = list.stream()
                    .map(o -> objectMapper.convertValue(o, ShareFileDTO.class))
                    .collect(Collectors.toList());

            return collect;
        }


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
    public String getSvipDlink(ShareFileDTO shareFileDTO,Long id) throws Exception{

        DbtableSvip nextSvip = dbtableSvipService.getNextSvip();
        ResponseEntity<String> responseEntity = requestService.requestSvipDlink(shareFileDTO.getDlink(),nextSvip.getSvipBduss());

        List<String> location = responseEntity.getHeaders().get("Location");

        if (Objects.isNull(location) && location.size()==0){
            //冻结
            dbtableSvipService.freezeSvip(nextSvip.getId());

            //再次获取
            nextSvip = dbtableSvipService.getNextSvip();
            responseEntity = requestService.requestSvipDlink(shareFileDTO.getDlink(),nextSvip.getSvipBduss());
            location = responseEntity.getHeaders().get("Location");

            //不存在
            if (Objects.isNull(location) && location.size()==0) return null;
        }

        //添加流量
        boolean b = pandownUserFlowService.addUserFlow(id, shareFileDTO.getSize());

        if (!b) throw new RuntimeException("流量不足");

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
    public List<ShareFileDTO> getSvipDlink(GetSvipDlinkDTO getSvipDlinkDTO,Long id) throws Exception {
        String sign = getSvipDlinkDTO.getSign();
        Long timestamp = getSvipDlinkDTO.getTimestamp();
        String seckey = getSvipDlinkDTO.getSeckey();
        String shareid = getSvipDlinkDTO.getShareid();
        String uk = getSvipDlinkDTO.getUk();
        List<ShareFileDTO> shareFileList = getSvipDlinkDTO.getShareFileList();


        List result = new ArrayList();
        for (int i = 0; i < shareFileList.size(); i++) {
            ShareFileDTO shareFileDTO = shareFileList.get(i);

            String dlink = shareFileDTO.getDlink();
            //如果普通连接为空，去提取
            if (StringUtils.isBlank(dlink)){
                ArrayList fileIdList = new ArrayList();
                fileIdList.add(shareFileDTO.getFs_id());
                GetDlinkDTO getDlinkDTO = new GetDlinkDTO();
                getDlinkDTO.setFsIdList(fileIdList);
                getDlinkDTO.setUk(uk);
                getDlinkDTO.setSeckey(seckey);
                getDlinkDTO.setSign(sign);
                getDlinkDTO.setTimestamp(timestamp);
                getDlinkDTO.setShareid(shareid);
                //获取普通连接
                List<ShareFileDTO> shareFileDTOList = getDlink(getDlinkDTO);

                if (Objects.nonNull(shareFileDTOList) && shareFileDTOList.size()>0){
                    shareFileDTO = shareFileDTOList.get(0);
                }
            }

            //获取SVIP连接

            String link = this.getSvipDlink(shareFileDTO,id);
            if (StringUtils.isBlank(link)) continue;
            //填充SvipDlink字段
            shareFileDTO.setSvipDlink(link);


            result.add(shareFileDTO);
        }
        return result;
    }

    public List<ShareFileDTO> getAllSvipdLink(ListAllSvipDlinkDTO listAllSvipDlinkDTO,Long id) throws Exception {
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
            String link = getSvipDlink(shareFileDTO,id);
            shareFileDTO.setSvipDlink(link);
            result.add(shareFileDTO);
        }

        return result;
    }
}
