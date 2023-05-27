package com.pan.pandown;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pan.pandown.api.RequestService;
import com.pan.pandown.service.download.DownloadService;
import com.pan.pandown.util.DTO.downloadApi.GetDlinkDTO;
import com.pan.pandown.util.DTO.downloadApi.SignAndTimeDTO;
import com.pan.pandown.web.PandownApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author yalier(wenyao)
 * @since 2023/4/28
 * @Description DownloadService test
 */
@SpringBootTest(classes = PandownApplication.class)
public class DownloadApiTest {
    @Autowired
    private DownloadService downloadService;

    @Autowired
    private RequestService requestService;

    @Test
    public void testDownloadService() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String surl = "1P-aqpIgAD4nZiBZb3gmg2w";
        String pwd = "02um";

        /*Map map = downloadService.listDir(surl, pwd, "", 1);
        System.out.println(map);

        List list = downloadService.listFiles(surl, pwd,"", new ArrayList());
        System.out.println(objectMapper.writeValueAsString(list));

        Map shareData = downloadService.listShare(surl, pwd);
        System.out.println(objectMapper.writeValueAsString(shareData));


        ResponseEntity<Map> responseEntity = downloadService.requestSignAndTime(surl);
        System.out.println(responseEntity.getHeaders().get("Set-Cookie").get(1));*/


        SignAndTimeDTO signAndTime = downloadService.getSignAndTime(surl);
        System.out.println(signAndTime);
    }

    @Test
    public void testSvipDlink(){
        String surl = "1xlNlsoQwb--rJdefwslqrQ";
        String pwd = "9r7c";

        SignAndTimeDTO signAndTime = downloadService.getSignAndTime(surl);
        System.out.println(signAndTime);
        Object timestamp = signAndTime.getTimestamp();
        Object sign = signAndTime.getSign();

        GetDlinkDTO downloadApiDTO = new GetDlinkDTO();
        downloadApiDTO.setFsIdList(new ArrayList(){{add("292608207123055");add("979913794958665");}});

        downloadApiDTO.setShareid("59930918115");
        downloadApiDTO.setUk("1127198855");
        downloadApiDTO.setTimestamp(Long.parseLong(timestamp.toString()));
        downloadApiDTO.setSign(sign.toString());
        downloadApiDTO.setSeckey("FjsaDdahS3NCh18koEgh9uB1XztK9yUr4f9nt6ydoLE~");
        ResponseEntity<Map> responseEntity = requestService.requestDlink(downloadApiDTO);
        System.out.println(responseEntity);
        List dlink = (List)downloadService.getDlink(downloadApiDTO);
        Map o = (Map) dlink.get(0);
        //Object dlink1 = downloadService.getSvipDlink(dlink);
        // Object dlink1 = downloadService.getSvipDlink(o.get("dlink").toString());
        //System.out.println(dlink1);
    }

    @Test
    public void testListDir(){
        String surl = "1xlNlsoQwb--rJdefwslqrQ";
        String pwd = "9r7c";
        String dir = "/游戏/Call.of.Duty.Modern.Warfare.2.Remastered";

        Map map = downloadService.listDir(surl, pwd, dir, 1);
        System.out.println(map);
    }
}
