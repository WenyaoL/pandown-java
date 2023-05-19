package com.pan.pandown.service.download;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pan.pandown.api.RequestService;
import com.pan.pandown.dao.entity.DbtableCommonAccount;
import com.pan.pandown.dao.entity.DbtableSvip;
import com.pan.pandown.service.IDbtableCommonAccountService;
import com.pan.pandown.service.IDbtableSvipService;
import com.pan.pandown.util.DTO.downloadApi.*;
import com.pan.pandown.util.redis.RedisService;
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
     * @param dlink
     * @return
     */
    public String getSvipDlink(String dlink){

        DbtableSvip nextSvip = dbtableSvipService.getNextSvip();
        ResponseEntity<String> responseEntity = requestService.requestSvipDlink(dlink,nextSvip.getSvipBduss());

        List<String> location = responseEntity.getHeaders().get("Location");

        if (Objects.isNull(location) && location.size()==0){
            //冻结
            dbtableSvipService.freezeSvip(nextSvip.getId());

            //再次获取
            nextSvip = dbtableSvipService.getNextSvip();
            responseEntity = requestService.requestSvipDlink(dlink,nextSvip.getSvipBduss());
            location = responseEntity.getHeaders().get("Location");

            //不存在
            if (Objects.isNull(location) && location.size()==0) return null;
        }

        return location.get(0);
    }

    /**
     * svip下载链接获取
     * @param dlinkList
     * @return
     */
    public List getSvipDlink(List<ShareFileDTO> dlinkList){
        List result = new ArrayList();
        for (int i = 0; i < dlinkList.size(); i++) {
            String dlink = dlinkList.get(i).getDlink().toString();
            String location = this.getSvipDlink(dlink);
            if (StringUtils.isBlank(location)) continue;
            result.add(location);
        }
        return result;
    }

    public Object getAllSvipdLink(ListAllSvipDlinkDTO listAllSvipDlinkDTO){
        String surl = listAllSvipDlinkDTO.getSurl();
        String pwd = listAllSvipDlinkDTO.getPwd();
        String seckey = listAllSvipDlinkDTO.getSeckey();
        String uk = listAllSvipDlinkDTO.getUk();
        String shareid = listAllSvipDlinkDTO.getShareid();
        List<ShareFileDTO> shareFileDTOList = listAllSvipDlinkDTO.getShareFileList();

        ArrayList<String> fileidList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //解析所有文件
        for (int i = 0;i<shareFileDTOList.size();i++){
            ShareFileDTO shareFile = shareFileDTOList.get(i);
            int isdir = shareFile.getIsdir();
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
        List<ShareFileDTO> list = getDlink(getDlinkDTO);

        //用dlink换svipDlink
        for (int i = 0; i < list.size(); i++) {
            ShareFileDTO shareFileDTO = list.get(i);
            String dlink = list.get(i).getDlink();
            String svipDlink = getSvipDlink(dlink);
            shareFileDTO.setSvipDlink(svipDlink);
        }

        return list;
    }
}
