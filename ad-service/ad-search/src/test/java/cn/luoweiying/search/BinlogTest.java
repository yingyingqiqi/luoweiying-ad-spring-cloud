package cn.luoweiying.search;

import cn.luoweiying.ApplicationTest;
import cn.luoweiying.mysql.BinlogConfig;
import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationTest.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BinlogTest {
    @Autowired
    private final BinlogConfig config;

    public BinlogTest() {
        config = new BinlogConfig();
    }

    @Test
    public void binlogTest() {
        BinaryLogClient client = new BinaryLogClient(
                config.getHost(),
                config.getPort(),
                config.getUsername(),
                config.getPassword()
        );
        if (!StringUtils.isEmpty(config.getBinlogName()) && !config.getPosition().equals(-1L)) {
            client.setBinlogFilename(config.getBinlogName());
            client.setBinlogPosition(config.getPosition());
        }
        client.registerEventListener((event) -> {
            EventData data = event.getData();

            if (data instanceof UpdateRowsEventData) {
                System.out.println(event.getHeader().getEventType().name());
                System.out.println("--------------");
                System.out.println(event.toString());
                System.out.println(((UpdateRowsEventData) data).getRows());
                System.out.println("update-------");
                System.out.println(data.toString());
            } else if (data instanceof WriteRowsEventData) {
                System.out.println("write----");
                System.out.println(data.toString());
            } else if (data instanceof DeleteRowsEventData) {
                System.out.println("delete-------");
                System.out.println(data.toString());
            }
        });
        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
