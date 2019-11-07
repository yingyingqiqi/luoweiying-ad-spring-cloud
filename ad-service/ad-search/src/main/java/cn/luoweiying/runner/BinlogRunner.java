package cn.luoweiying.runner;

import cn.luoweiying.mysql.BinlogClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class BinlogRunner implements ApplicationRunner {
    @Autowired
    BinlogClient binlogClient;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("coming to BinlogRunner");
      binlogClient.connet();
    }
}
