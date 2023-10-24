package com.pan.pandown.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pan.pandown.apiModel.BaiduApiErrorNo;
import com.pan.pandown.util.DTO.downloadApi.GetDlinkDTO;
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

    @Value("${pandown.downloadApi.getFileList.url}")
    private String fileListUrl;

    @Value("${pandown.downloadApi.getSignAndTime.url}")
    private String tplconfigUrl;

    @Value("${pandown.downloadApi.getDlink.url}")
    private String getDlinkUrl;

    @Value("${pandown.downloadApi.getAccountState.url}")
    private String getAccountStateUrl;


    @Value("${pandown.Account.cookies}")
    private String cookieStr; //静态cookies字符串

    private Map<String, String> cookies = new ConcurrentHashMap<>();


    /**
     * 文件列表网络请求(默认使用配置文件中的静态cookie的bduss)
     * @param surl 分享surl
     * @param pwd 分享验证码
     * @param dir 解析目录
     * @param page 请求页码
     * @return
     */
    public ResponseEntity<Map> requestFileList(String surl, String pwd, String dir, Integer page){
        String bduss = cookies.get("BDUSS");
        if (StringUtils.isBlank(bduss)) {
            cookies.putAll(strToCookieMap(cookieStr));
            bduss = cookies.get("BDUSS");
        }
        return requestFileList(surl, pwd, dir, page,bduss);
    }

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
        URI uri = UriComponentsBuilder.fromHttpUrl(fileListUrl)
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

        if(StringUtils.isBlank(bduss)) bduss = cookies.get("BDUSS");

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
     *
     * @param surl
     * @return
     */
    public ResponseEntity<Map> requestSignAndTime(String surl){
        String BAIDUID = cookies.get("BAIDUID");
        if (StringUtils.isBlank(BAIDUID)) {
            cookies.putAll(strToCookieMap(cookieStr));
            BAIDUID = cookies.get("BAIDUID");
        }
        return requestSignAndTime(surl,BAIDUID);
    }

    /**
     * @param surl
     * @return
     */
    public ResponseEntity<Map> requestSignAndTime(String surl,String BAIDUID) {
        URI uri = UriComponentsBuilder.fromHttpUrl(tplconfigUrl)
                .queryParam("surl", surl)
                .queryParam("fields", "sign,timestamp")
                .queryParam("channel", "chunlei")
                .queryParam("web", 1)
                .queryParam("app_id", 250528)
                .queryParam("clienttype", 0)
                .build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        //生成cookie
        List<String> cookieList = mapToCookieList(new HashMap<String, String>() {{
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


    public ResponseEntity<Map> requestDlink(GetDlinkDTO getDlinkDTO){
        if (this.cookies.size() == 0) this.cookies.putAll(strToCookieMap(cookieStr));
        return requestDlink(getDlinkDTO,this.cookies);
    }

    public ResponseEntity<Map> requestDlink(GetDlinkDTO getDlinkDTO,Map cookieMap) {
        URI uri = UriComponentsBuilder.fromHttpUrl(getDlinkUrl)
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


        headers.put(HttpHeaders.COOKIE,mapToCookieList(cookieMap));
        //headers.set("Cookie","BDUSS=l-VWV5YjRhd1BkR1BmQk1kR21LME9HVGtUN2RQWDUxazNoQXVpQ0dSOE1GbmhrSVFBQUFBJCQAAAAAAAAAAAEAAAD-Q1grYTIyNjkyNzZlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAyJUGQMiVBkTj;");
        //headers.set("Cookie", "BAIDU_WISE_UID=wapp_1676293108995_690; PANWEB=1; PSTM=1677429102; ZFY=5E:BJorOCHA7Wi5smW4uldUw:AoNlZQUcQkL9Sb2RmVEE:C; BIDUPSID=56C0C0ED879D38BE155BE6D4F2C06331; BAIDUID=30434187CCBE170ED911242A3DEE58A8:FG=1; BAIDUID_BFESS=30434187CCBE170ED911242A3DEE58A8:FG=1; BDCLND=FjsaDdahS3MD8m2e1rB1Gh65rc9cBQL%2BXMs72C1b%2BqI%3D; csrfToken=IYF9E2PTNdgqsCT7Ti9XNIKV; newlogin=1; BDUSS=l-VWV5YjRhd1BkR1BmQk1kR21LME9HVGtUN2RQWDUxazNoQXVpQ0dSOE1GbmhrSVFBQUFBJCQAAAAAAAAAAAEAAAD-Q1grYTIyNjkyNzZlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAyJUGQMiVBkTj; BDUSS_BFESS=l-VWV5YjRhd1BkR1BmQk1kR21LME9HVGtUN2RQWDUxazNoQXVpQ0dSOE1GbmhrSVFBQUFBJCQAAAAAAAAAAAEAAAD-Q1grYTIyNjkyNzZlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAyJUGQMiVBkTj; STOKEN=30621ea24dd17717b4ccea5483afe0b7cf0a97b70404c449c0cea6497687ecfb; ndut_fmt=6AA7CF2196CC37D632A87EF6AB15A124F5D2010FDB5C70425E00E59F789C8BB9; ab_sr=1.0.1_MmEyODQ0MjI0OTEyY2E2ZjU5YjgxYjY5OGY5ZGFlYTI4YmM5OGMyYjc2MjBhMGY1ZDMwYmQwYzdmYTZhOTQ0OTQ5YjcxYjYyNDQ5Y2E5ZDhkOTkyMzBmYTRiYWQ5NWMyZDEwOTNiN2I4MTVkYzlhMjBlZTkyYjdhOGU0Zjk5N2VkMThmYjdjZmU5Y2ZkM2RhMjJiOGYxZmRlMWI3NmM2MzBmODM2ZDAzOTUwOGNiM2UzNGIyN2EyN2E4OTA2MWU4; PANPSC=10660478181080867701%3AeDVC44VMlkuwpVgviK7pHVcS2d9ns3O5WSpAOtiYnuooK5JNovHgnDJ3TFL5%2B7J8K9sKmG0jScziRClWYPNr2cPKVjvvBMbo2uXu4bOTdjc2jyb7JodIivKBB5%2BWSCTpbaC532AuNi3w0iRW66wpcQelhSA6FDy8slZuWNt1qytZJEFwtmF4sQzCIHAAYipkAsGNr4paWK3EGlSCrabBxOtuQFScyIV%2FkrFWWqsgPEw%3D");
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(uri, httpEntity, Map.class);

        //网络异常
        if (!responseEntity.getStatusCode().is2xxSuccessful()){
            throw new RuntimeException("请求网络异常:"+uri);
        }

        return responseEntity;
    }

    public ResponseEntity<String> requestSvipDlink(String dlink, String BDUSS) {
        URI uri = null;
        try {
            uri = new URI(dlink);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "LogStatistic");
        headers.put("Cookie", mapToCookieList(new HashMap<String, String>() {{ put("BDUSS", BDUSS); }}));
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(headers);
        //head请求(返回的响应是重定向响应,code:302)
        ResponseEntity<String> response = restTemplate.exchange(uri,HttpMethod.HEAD, httpEntity, String.class);

        return response;
    }

    public ResponseEntity<Map> requestAccountState(String bduss,String stoken){
        URI uri = UriComponentsBuilder.fromHttpUrl(getAccountStateUrl)
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
        headers.put(HttpHeaders.COOKIE,mapToCookieList(cookies));
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


    public List<String> mapToCookieList(Map<String,String> map){
        ArrayList<String> arrayList = new ArrayList<>();
        map.forEach((k,v)->{
            arrayList.add(k + "=" + v);
        });
        return arrayList;
    }

    public Map<String,String> strToCookieMap(String cookieStr){
        if (Objects.isNull(cookieStr))return null;
        String[] split = cookieStr.split(";");
        HashMap<String, String> result = new HashMap<>();
        Arrays.stream(split).forEach(str->{
            String[] keyAndValue = str.trim().split("=");
            if (keyAndValue.length == 2){
                String key = keyAndValue[0];
                String value = keyAndValue[1];
                result.put(key,value);
            }

        });

        return result;
    }


}