package cn.luoweiying.mysql.dto;

import cn.luoweiying.mysql.constant.OpType;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

//将JsonTable、Template对象，解析为ParseTemplate
//感觉像再次解析  重复 ？？
@Data
public class ParseTemplate {
    private String database;
    //表名  ->  列索引及列名
    private Map<String, TableTemplate> map = new HashMap<>();

    public static ParseTemplate parse(Template template) {
        ParseTemplate parseTemplate = new ParseTemplate();

        String databaseName = template.getDatabase();
        parseTemplate.setDatabase(databaseName);

        List<JsonTable> tabeleList = template.getTableList();
        //遍历JsonTable对象， 获取表名、表层级、  列的crud
        for (JsonTable table : tabeleList) {
            String tableName = table.getTableName();
            Integer level = table.getLevel();

            //填充tableTemplate
            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setTabelName(tableName);
            tableTemplate.setLevel(level.toString());
            //遍历操作类型的列  ，填充tableTemplate的两个Map
            Map<OpType, List<String>> opTypeFieldSetMap = tableTemplate.getOpTypeFieldSetMap();
            setOpTypeFieldSetMap(table, opTypeFieldSetMap);
            //posMap在调用此对象的类查询数据库 填充。 TemplateHolder.loadMeta
            // 为什么不在这个类填充？？  难道时数据库的连接对象？ 假如传连接对象过来？？

            //存入Map<String, TableTemplate> K:表名 V:适用binlog日志的表对象。
            parseTemplate.getMap().put(tableName, tableTemplate);
            /*for (JsonTable.Column column : table.getInsert()) {
                getAndCreateIfNeed(OpType.ADD,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }
            for (JsonTable.Column column : table.getUpdate()) {
                getAndCreateIfNeed(OpType.UPDATE,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }
            for (JsonTable.Column column : table.getDelete()) {
                getAndCreateIfNeed(OpType.DELETE,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumn());
            }*/
        }
        return parseTemplate;
    }

    private static void setOpTypeFieldSetMap(JsonTable table, Map<OpType, List<String>> opTypeFieldSetMap) {
        for (JsonTable.Column column : table.getInsert()) {
            getAndCreateIfNeed(OpType.ADD,
                    opTypeFieldSetMap,
                    ArrayList::new
            ).add(column.getColumn());
        }
        for (JsonTable.Column column : table.getUpdate()) {
            getAndCreateIfNeed(OpType.UPDATE,
                    opTypeFieldSetMap,
                    ArrayList::new
            ).add(column.getColumn());
        }
        for (JsonTable.Column column : table.getDelete()) {
            getAndCreateIfNeed(OpType.DELETE,
                    opTypeFieldSetMap,
                    ArrayList::new
            ).add(column.getColumn());
        }
    }
    private static <T, R> R getAndCreateIfNeed(T key, Map<T, R> map, Supplier<R> function) {
        return map.computeIfAbsent(key, k -> function.get());
    }

}
