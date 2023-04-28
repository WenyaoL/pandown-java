package com.pan.pandown;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pan.pandown.service.download.DownloadService;
import com.pan.pandown.util.security.JWTService;
import com.pan.pandown.web.PandownApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = PandownApplication.class)
public class DownloadApiTest {
    @Autowired
    private DownloadService downloadService;



    @Test
    public void testDownloadService() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String surl = "1P-aqpIgAD4nZiBZb3gmg2w";
        String pwd = "02um";

        Map map = downloadService.listDir(surl, pwd, "", 1);
        System.out.println(map);

        List list = downloadService.listFiles(surl, pwd,"", new ArrayList());
        System.out.println(objectMapper.writeValueAsString(list));

        Map shareData = downloadService.listShare(surl, pwd);
        System.out.println(objectMapper.writeValueAsString(shareData));
    }
}
