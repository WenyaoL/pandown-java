package com.pan.pandown.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-04-28
 */
@Service
public class RequestService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${pandown.downloadApi.getFileList.url}")
    private String fileListUrl;

    @Value("${pandown.BDUSS}")
    private String bduss;

    private Map<String,String> cookies = new HashMap<>();

    /***
     * 文件列表网络请求
     * @param surl 分享surl
     * @param pwd 分享验证码
     * @param dir 解析目录
     * @param page 请求页码
     * @return
     */
    public ResponseEntity<Map> requestFileList(String surl, String pwd, String dir, Integer page){
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

    /**
     *
     * @param surl
     * @return
     */
    public ResponseEntity<Map> requestSignAndTime(String surl){
        URI uri = UriComponentsBuilder.fromHttpUrl("https://pan.baidu.com/share/tplconfig")
                .queryParam("surl", surl)
                .queryParam("fields", "sign,timestamp")
                .queryParam("channel", "chunlei")
                .queryParam("web", 1)
                .queryParam("app_id", 250528)
                .queryParam("clienttype", 0)
                .build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie","BAIDUID=".concat(cookies.getOrDefault("baiduId","")));
        headers.set("User-Agent","netdisk;pan.baidu.com");


        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Map> exchange = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Map.class);

        return exchange;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public void addCookie(String key,String value){
        this.cookies.put(key,value);
    }
}
