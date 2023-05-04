package com.pan.pandown.service.download;

import com.pan.pandown.api.RequestService;
import com.pan.pandown.util.DTO.DownloadApiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class DownloadService {

    @Autowired
    private RequestService requestService;

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
            data.put("list",listFiles(surl,pwd,"",new ArrayList()));
            return data;
        }

        return null;
    }

    /**
     *
     * @param surl 分享surl
     * @param pwd 分享验证码
     * @param dir 解析目录
     * @param saveList 存储列表
     * @return 存储列表
     */
    public List listFiles(String surl,String pwd,String dir, List saveList){
        if (saveList == null) saveList = new ArrayList();
        List<Map> dataList = (List)listDir(surl, pwd, dir, 1).get("list");

        for (int i = 0; i < dataList.size(); i++) {
            Map file = dataList.get(i);
            if (Integer.parseInt(file.getOrDefault("isdir",0).toString()) == 1){
                String path = (String) file.get("path");
                List list = listFiles(surl, pwd, path, new ArrayList<>());
                file.put("children",list);
            }
            saveList.add(file);
        }
        return saveList;
    }

    /**
     * 列举分享文件夹
     * @param surl 分享surl
     * @param pwd 分享验证码
     * @param dir 解析目录
     * @param page 请求页码
     * @return
     */
    public Map listDir(String surl,String pwd,String dir,Integer page){
        ResponseEntity<Map> responseEntity = requestService.requestFileList(surl, pwd, dir, page);
        return (Map) responseEntity.getBody().getOrDefault("data",null);
    }




    public Map getSignAndTime(String surl){
        ResponseEntity<Map> responseEntity = requestService.requestSignAndTime(surl);

        String errno = responseEntity.getBody().get("errno").toString();

        if (errno.equals("0")){
            return (Map) responseEntity.getBody().get("data");
        }else{
            //请求失败，更新cookie重新请求一次
            List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
            cookies.forEach(cookie->{
                String baiduId = cookie.startsWith("BAIDUID=") ? cookie.split(";")[0].substring(8):"";
                requestService.addCookie("baiduId",baiduId);

            });
            //重新请求一次
            responseEntity = requestService.requestSignAndTime(surl);
            errno = responseEntity.getBody().get("errno").toString();
            return errno.equals("0") ? (Map) responseEntity.getBody().get("data") :null;
        }

    }


    public Object getDlink(DownloadApiDTO downloadApiDTO){
        ResponseEntity<Map> responseEntity = requestService.requestDlink(downloadApiDTO);
        return responseEntity.getBody().getOrDefault("list",null);
    }


    public Object getSvipDlink(String dlink){
        ResponseEntity<String> responseEntity = requestService.requestSvipDlink(dlink);
        return responseEntity.getHeaders().get("Location").get(0);
    }
}
