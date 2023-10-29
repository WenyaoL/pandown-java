package com.pan.pandown.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pan.pandown.util.CookieUtils;
import com.pan.pandown.util.DTO.downloadApi.GetDlinkDTO;
import com.pan.pandown.util.configuration.RequestServiceConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-04-28
 */
@Service
@Slf4j
public class RequestService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RequestServiceConfiguration requestServiceConfiguration;




    /***
     * 文件列表网络请求
     * @param surl 分享surl
     * @param pwd 分享验证码
     * @param dir 解析目录
     * @param page 请求页码
     * @param bduss 普通账号的bduss
     * @return
     */
    public ResponseEntity<Map> requestFileList(String surl, String pwd, String dir, Integer page,String bduss) {
        if(StringUtils.isBlank(bduss)) throw new RuntimeException("缺失bduss");

        URI uri = UriComponentsBuilder.fromHttpUrl(requestServiceConfiguration.getFilelistUrl())
                .queryParam("channel", "weixin")
                .queryParam("version", "2.2.2")
                .queryParam("clienttype", 25)
                .queryParam("web", 1)
                .build().encode().toUri();


        //请求体(表单形式)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("shorturl", surl);
        body.add("dir", dir);
        if (StringUtils.isNotBlank(dir)) {
            body.add("root", 0);
        } else {
            body.add("root", 1);
        }
        body.add("pwd", pwd);
        body.add("page", page.intValue());
        body.add("num", 1000);
        body.add("order", "time");

        //请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent", "netdisk");
        headers.set("Cookie", "BDUSS=".concat(bduss));

        //请求实例
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(uri, httpEntity, Map.class);

        //网络异常
        if (!responseEntity.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("请求网络异常:"+surl);
        }

        return responseEntity;
    }



    /**
     * @param surl
     * @return
     */
    public ResponseEntity<Map> requestSignAndTime(String surl,String BAIDUID) {
        URI uri = UriComponentsBuilder.fromHttpUrl(requestServiceConfiguration.getSigntimeUrl())
                .queryParam("surl", surl)
                .queryParam("fields", "sign,timestamp")
                .queryParam("channel", "chunlei")
                .queryParam("web", 1)
                .queryParam("app_id", 250528)
                .queryParam("clienttype", 0)
                .build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        //生成cookie
        List<String> cookieList = CookieUtils.mapToCookieList(new HashMap<String, String>() {{
            put("BAIDUID", BAIDUID);
        }});
        headers.put(HttpHeaders.COOKIE, cookieList);
        headers.set("User-Agent", "netdisk;pan.baidu.com");


        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Map> exchange = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Map.class);

        //网络异常
        if (!exchange.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("请求网络异常:"+surl);
        }

        return exchange;
    }




    public ResponseEntity<Map> requestDlink(GetDlinkDTO getDlinkDTO,String cookie) {
        URI uri = UriComponentsBuilder.fromHttpUrl(requestServiceConfiguration.getDlinkUrl())
                .queryParam("app_id", 250528)
                .queryParam("channel", "chunlei")
                .queryParam("clienttype", 12)
                .queryParam("sign", getDlinkDTO.getSign())
                .queryParam("timestamp", getDlinkDTO.getTimestamp())
                .queryParam("web", 1)
                .encode().build().toUri();

        //请求体(表单形式)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.set("encrypt", 0);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String sekey = objectMapper.writeValueAsString(new HashMap<String, String>() {{
                put("sekey", getDlinkDTO.getSeckey());
            }});
            body.set("extra",sekey);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        body.set("fid_list", getDlinkDTO.getFsIdList());
        body.set("primaryid", getDlinkDTO.getShareid());
        body.set("uk", getDlinkDTO.getUk());
        body.set("product", "share");
        body.set("type", "nolimit");

        //请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.69");
        headers.set("Referer", "https://pan.baidu.com/disk/home");
        headers.set(HttpHeaders.COOKIE,cookie);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(uri, httpEntity, Map.class);

        //网络异常
        if (!responseEntity.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("请求网络异常:"+uri);
        }

        return responseEntity;
    }

    public ResponseEntity<String> requestSvipDlink(String dlink, String BDUSS) {
        URI uri  = null;
        try {
            uri = new URI(dlink);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "LogStatistic");
        headers.put("Cookie", CookieUtils.mapToCookieList(new HashMap<String, String>() {{ put("BDUSS", BDUSS); }}));
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(headers);
        //head请求(返回的响应是重定向响应,code:302)
        try {
            ResponseEntity<String> response = restTemplate.exchange(uri,HttpMethod.HEAD, httpEntity, String.class);
            return response;
        }catch (Exception e){
            throw new RuntimeException("请求SVIP下载链接时出错");
        }

    }

    public ResponseEntity<Map> requestAccountState(String bduss,String stoken){
        URI uri = UriComponentsBuilder.fromHttpUrl(requestServiceConfiguration.getAccountstateUrl())
                .queryParam("app_id", 250528)
                .queryParam("channel", "chunlei")
                .queryParam("clienttype", 0)
                .queryParam("web", 1)
                .encode().build().toUri();

        //请求头
        HttpHeaders headers = new HttpHeaders();
        //生成cookies
        HashMap<String, String> cookies = new HashMap<String, String>() {{
            put("BDUSS", bduss);
            put("STOKEN", stoken);
        }};
        headers.put(HttpHeaders.COOKIE, CookieUtils.mapToCookieList(cookies));
        headers.set(HttpHeaders.USER_AGENT,"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36 Edg/110.0.1587.69");

        //请求体(表单形式)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.set("fields","[\"username\",\"loginstate\",\"is_vip\",\"is_svip\",\"is_evip\"]");

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(uri, httpEntity, Map.class);

        //网络异常
        if (!responseEntity.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("请求网络异常:"+uri);
        }

        return responseEntity;
    }





}