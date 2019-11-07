package cn.luoweiying.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

import java.io.IOException;

//WriteRowsEventData{tableId=109, includedColumns={0, 1, 2}, rows=[[8, 10, 测试]]}
//Event{header=EventHeaderV4{timestamp=1572230179000, eventType=EXT_UPDATE_ROWS, serverId=1, headerLength=19, dataLength=49, nextPosition=1987, flags=0}, data=UpdateRowsEventData{tableId=109, includedColumnsBeforeUpdate={0, 1, 2}, includedColumns={0, 1, 2}, rows=[
//    {before=[2, 10, 测试], after=[2, 10, 奔驰]}]}}
//date  Tue Jan 01 08:00:00 CST 2019
public class BinlogServiceTest {
    public static void main(String[] args) throws IOException {
        BinaryLogClient client = new BinaryLogClient(
                "127.0.0.1",
                3306,
                "",
                "");
/*        EventDeserializer eventDeserializer = new EventDeserializer();
        eventDeserializer.setCompatibilityMode(
                EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
                EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY
        );
        client.setEventDeserializer(eventDeserializer);*/
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
        client.connect();
    }

}
