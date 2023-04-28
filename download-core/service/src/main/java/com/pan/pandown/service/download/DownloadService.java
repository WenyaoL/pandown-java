package com.pan.pandown.service.download;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class DownloadService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${pandown.downloadApi.getFileList.url}")
    private String fileListUrl;

    @Value("${pandown.BDUSS}")
    private String bduss;

    /**
     * 返回所有分享信息
     * @param surl
     * @param pwd
     * @return
     */
    public Map listShare(String surl,String pwd){
        ResponseEntity<Map> responseEntity = requestFileList(surl, pwd, "", 1);
        Map data = (Map) responseEntity.getBody().get("data");
        //链接有效检验
        if(Objects.nonNull(data) && Objects.nonNull(data.get("list")) && ((List)data.get("list")).size()>0){
            data.put("list",listFiles(surl,pwd,"",new ArrayList()));
            return data;
        }

        return null;
    }


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
     * @param surl
     * @param pwd
     * @param dir
     * @param page
     * @return
     */
    public Map listDir(String surl,String pwd,String dir,Integer page){
        ResponseEntity<Map> responseEntity = requestFileList(surl, pwd, dir, page);
        return (Map) responseEntity.getBody().getOrDefault("data",null);
    }


    /***
     * 文件列表网络请求
     * @param surl
     * @param pwd
     * @param dir
     * @param page
     * @return
     */
    public ResponseEntity<Map> requestFileList(String surl,String pwd,String dir,Integer page){
        //请求体(表单形式)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("shorturl",surl);
        body.add("dir",dir);
        if(StringUtils.hasText(dir)){
            body.add("root",0);
        }else {
            body.add("root",1);
        }
        body.add("pwd",pwd);
        body.add("page",page.intValue());
        body.add("num",1000);
        body.add("order","time");

        //请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent","netdisk");
        headers.set("Cookie","BDUSS=".concat(bduss));

        //请求实例
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(fileListUrl, httpEntity, Map.class);
        return responseEntity;
    }

}
