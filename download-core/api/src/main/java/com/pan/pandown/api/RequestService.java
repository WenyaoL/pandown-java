package com.pan.pandown.api;

import com.pan.pandown.util.DTO.DownloadApiDTO;
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

    @Value("${pandown.downloadApi.getSignAndTime.url}")
    private String tplconfigUrl;

    @Value("${pandown.downloadApi.getDlink.url}")
    private String getDlinkUrl;

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
        URI uri = UriComponentsBuilder.fromHttpUrl(fileListUrl)
                .queryParam("channel", "weixin")
                .queryParam("version", "2.2.2")
                .queryParam("clienttype", 25)
                .queryParam("web", 1)
                .build().encode().toUri();


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

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(uri, httpEntity, Map.class);
        return responseEntity;
    }

    /**
     *
     * @param surl
     * @return
     */
    public ResponseEntity<Map> requestSignAndTime(String surl){
        URI uri = UriComponentsBuilder.fromHttpUrl(tplconfigUrl)
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

    public ResponseEntity<Map> requestDlink(DownloadApiDTO downloadApiDTO){
        URI uri = UriComponentsBuilder.fromHttpUrl(getDlinkUrl)
                .queryParam("app_id", 250528)
                .queryParam("channel", "chunlei")
                .queryParam("clienttype", 12)
                .queryParam("sign", downloadApiDTO.getSign())
                .queryParam("timestamp", downloadApiDTO.getTimestamp())
                .queryParam("web", 1)
                .encode().build().toUri();

        //请求体(表单形式)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.set("encrypt",0);
        body.set("extra",new HashMap<String,String>(){{put("sekey",downloadApiDTO.getSekey());}});
        body.set("fid_list",downloadApiDTO.getFsIdList());
        body.set("primaryid",downloadApiDTO.getPrimaryid());
        body.set("uk",downloadApiDTO.getUk());
        body.set("product","share");
        body.set("type","nolimit");

        //请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.69");
        headers.set("Referer","https://pan.baidu.com/disk/home");
        headers.set("Cookie","BDUSS=".concat(bduss));

        //请求实例
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(uri, httpEntity, Map.class);

        return responseEntity;
    }


    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public void addCookie(String key,String value){
        this.cookies.put(key,value);
    }
}
