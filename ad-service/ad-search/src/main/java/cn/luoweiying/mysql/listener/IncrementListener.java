package cn.luoweiying.mysql.listener;

import cn.luoweiying.mysql.constant.Constant;
import cn.luoweiying.mysql.constant.OpType;
import cn.luoweiying.mysql.dto.BinlogRowData;
import cn.luoweiying.mysql.dto.MySqlRowData;
import cn.luoweiying.mysql.dto.TableTemplate;
import cn.luoweiying.mysql.sender.ISender;
import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class IncrementListener implements IListener {
    @Resource(name = "indexSender")
    private ISender sender;
    @Resource(name = "kafkaSender")
    private ISender senderKafka;    //kafka投递，方便后续业务

    @Autowired
    private final AggregationListener aggregationListener;

    public IncrementListener(AggregationListener aggregationListener) {
        this.aggregationListener = aggregationListener;
    }

    @Override
    @PostConstruct
    public void register() {
        log.info("IncrementListener register db table info");
        Constant.table2Db.forEach((k, v)-> aggregationListener.register(v,k,this));
    }

    @Override
    public void OnEvent(BinlogRowData binlogRowData) {
        EventType eventType = binlogRowData.getEventType();
        TableTemplate tableTemplate = binlogRowData.getTableTemplate();
        OpType type = OpType.to(eventType);
        //填充
        MySqlRowData mySqlRowData = new MySqlRowData();
        mySqlRowData.setOpType(type);
        mySqlRowData.setTableName(tableTemplate.getTabelName());
        mySqlRowData.setLevel(tableTemplate.getLevel());

        //取出json模板， 根据OpType 类型 可使用的列名
        List<String> fieldList = binlogRowData.getTableTemplate().getOpTypeFieldSetMap().get(type);
        if (null == fieldList) {
            log.warn("{} not support for {}" , type, tableTemplate.getTabelName());
            return;
        }
        //因为MySqlRowData已经生成map对象 ， 所以遍历写入
        for (Map<String, String> afterMap : binlogRowData.getAfter()) {
            Map<String, String> _afterMap = new HashMap<>();
            afterMap.entrySet().forEach(x -> _afterMap.put(x.getKey(),x.getValue()));
/*            for (Map.Entry<String, String> entry : afterMap.entrySet()) {
                String colName = entry.getKey();
                String colValue = entry.getValue();
                _afterMap.put(colName, colValue);
            }*/
            mySqlRowData.getFieldValueMap().add(_afterMap);
        }
        sender.sender(mySqlRowData);
        senderKafka.sender(mySqlRowData);//kafka投递，方便后续业务
    }
}
