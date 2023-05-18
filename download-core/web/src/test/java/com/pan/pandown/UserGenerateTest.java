package com.pan.pandown;

import com.pan.pandown.dao.entity.DbtableSvip;
import com.pan.pandown.service.IDbtableSvipService;
import com.pan.pandown.util.mybatisPlus.SnowflakeGenerator;
import com.pan.pandown.web.PandownApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

/**
 * @author yalier(wenyao)
 * @Description
 * @since 2023-05-05
 */
@SpringBootTest(classes = PandownApplication.class)
public class UserGenerateTest {

    @Autowired
    private IDbtableSvipService dbtableSvipService;



    @Test
    public void addSvip(){
        DbtableSvip dbtableSvip = new DbtableSvip();
        dbtableSvip.setName("yali");
        dbtableSvip.setSvipBduss("BDUSS=UzdU1nUG1qYWJPTEVQfmRocmpXVnZxYn5Ea2ZDZVlTLWx-VjBNZnMwNFhtdE5pRVFBQUFBJCQAAAAAAAAAAAEAAADa8x10TW92ZUFib3kAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABcNrGIXDaxiQ3;");
        dbtableSvip.setSvipStoken("?");
        dbtableSvip.setState(1);
        dbtableSvip.setCreateTime(LocalDateTime.now());
        dbtableSvip.setUpdateTime(LocalDateTime.now());
        dbtableSvipService.save(dbtableSvip);
    }
}
