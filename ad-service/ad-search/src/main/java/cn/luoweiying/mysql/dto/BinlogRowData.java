package cn.luoweiying.mysql.dto;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;

import java.util.List;
import java.util.Map;

// 对mysql-binlog-connector-java 的Event 对象 转换成我我们需要的 类型
//binglog的数据结构
@Data
public class BinlogRowData {
    private TableTemplate tableTemplate;

    private EventType eventType;
    //???  map<columnName, columnValue>
    private List<Map<String, String>> after;

    private List<Map<String, String>> before;

}
