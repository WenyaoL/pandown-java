package com.pan.pandown.api;

import com.pan.pandown.util.DTO.downloadApi.DownloadApiDTO;
import com.pan.pandown.util.DTO.downloadApi.GetDlinkDTO;
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
import java.net.URISyntaxException;
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

    private Map<String, String> cookies = new HashMap<>();

    /***
     * 文件列表网络请求
     * @param surl 分享surl
     * @param pwd 分享验证码
     * @param dir 解析目录
     * @param page 请求页码
     * @return
     */
    public ResponseEntity<Map> requestFileList(String surl, String pwd, String dir, Integer page) {
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
        if (StringUtils.hasText(dir)) {
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
        return responseEntity;
    }

    /**
     * @param surl
     * @return
     */
    public ResponseEntity<Map> requestSignAndTime(String surl) {
        URI uri = UriComponentsBuilder.fromHttpUrl(tplconfigUrl)
                .queryParam("surl", surl)
                .queryParam("fields", "sign,timestamp")
                .queryParam("channel", "chunlei")
                .queryParam("web", 1)
                .queryParam("app_id", 250528)
                .queryParam("clienttype", 0)
                .build().encode().toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", "BAIDUID=".concat(cookies.getOrDefault("baiduId", "")));
        headers.set("User-Agent", "netdisk;pan.baidu.com");


        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<Map> exchange = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Map.class);

        return exchange;
    }

    public ResponseEntity<Map> requestDlink(GetDlinkDTO getDlinkDTO) {
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
        body.set("extra", new HashMap<String, String>() {{
            put("sekey", getDlinkDTO.getSeckey());
        }});
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
        //headers.set("Cookie","BDUSS=l-VWV5YjRhd1BkR1BmQk1kR21LME9HVGtUN2RQWDUxazNoQXVpQ0dSOE1GbmhrSVFBQUFBJCQAAAAAAAAAAAEAAAD-Q1grYTIyNjkyNzZlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAyJUGQMiVBkTj;");

        headers.set("Cookie", "BAIDU_WISE_UID=wapp_1676293108995_690; PANWEB=1; PSTM=1677429102; ZFY=5E:BJorOCHA7Wi5smW4uldUw:AoNlZQUcQkL9Sb2RmVEE:C; BIDUPSID=56C0C0ED879D38BE155BE6D4F2C06331; BAIDUID=30434187CCBE170ED911242A3DEE58A8:FG=1; BAIDUID_BFESS=30434187CCBE170ED911242A3DEE58A8:FG=1; BDCLND=FjsaDdahS3MD8m2e1rB1Gh65rc9cBQL%2BXMs72C1b%2BqI%3D; csrfToken=IYF9E2PTNdgqsCT7Ti9XNIKV; newlogin=1; BDUSS=l-VWV5YjRhd1BkR1BmQk1kR21LME9HVGtUN2RQWDUxazNoQXVpQ0dSOE1GbmhrSVFBQUFBJCQAAAAAAAAAAAEAAAD-Q1grYTIyNjkyNzZlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAyJUGQMiVBkTj; BDUSS_BFESS=l-VWV5YjRhd1BkR1BmQk1kR21LME9HVGtUN2RQWDUxazNoQXVpQ0dSOE1GbmhrSVFBQUFBJCQAAAAAAAAAAAEAAAD-Q1grYTIyNjkyNzZlAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAyJUGQMiVBkTj; STOKEN=30621ea24dd17717b4ccea5483afe0b7cf0a97b70404c449c0cea6497687ecfb; ndut_fmt=6AA7CF2196CC37D632A87EF6AB15A124F5D2010FDB5C70425E00E59F789C8BB9; ab_sr=1.0.1_MmEyODQ0MjI0OTEyY2E2ZjU5YjgxYjY5OGY5ZGFlYTI4YmM5OGMyYjc2MjBhMGY1ZDMwYmQwYzdmYTZhOTQ0OTQ5YjcxYjYyNDQ5Y2E5ZDhkOTkyMzBmYTRiYWQ5NWMyZDEwOTNiN2I4MTVkYzlhMjBlZTkyYjdhOGU0Zjk5N2VkMThmYjdjZmU5Y2ZkM2RhMjJiOGYxZmRlMWI3NmM2MzBmODM2ZDAzOTUwOGNiM2UzNGIyN2EyN2E4OTA2MWU4; PANPSC=10660478181080867701%3AeDVC44VMlkuwpVgviK7pHVcS2d9ns3O5WSpAOtiYnuooK5JNovHgnDJ3TFL5%2B7J8K9sKmG0jScziRClWYPNr2cPKVjvvBMbo2uXu4bOTdjc2jyb7JodIivKBB5%2BWSCTpbaC532AuNi3w0iRW66wpcQelhSA6FDy8slZuWNt1qytZJEFwtmF4sQzCIHAAYipkAsGNr4paWK3EGlSCrabBxOtuQFScyIV%2FkrFWWqsgPEw%3D");
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(uri, httpEntity, Map.class);

        return responseEntity;
    }

    public ResponseEntity<String> requestSvipDlink(String dlink,String BDUSS) {
        URI uri = UriComponentsBuilder.fromHttpUrl(dlink)
                .build()
                .toUri();

        BDUSS = "BDUSS=UzdU1nUG1qYWJPTEVQfmRocmpXVnZxYn5Ea2ZDZVlTLWx-VjBNZnMwNFhtdE5pRVFBQUFBJCQAAAAAAAAAAAEAAADa8x10TW92ZUFib3kAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABcNrGIXDaxiQ3;";
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "LogStatistic");
        headers.set("Cookie", BDUSS);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.postForEntity(uri, httpEntity, String.class);
        return response;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public void addCookie(String key, String value) {
        this.cookies.put(key, value);
    }
}