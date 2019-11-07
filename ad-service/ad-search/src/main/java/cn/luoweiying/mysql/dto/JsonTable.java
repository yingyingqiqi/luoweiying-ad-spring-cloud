package cn.luoweiying.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
//解析json数据中一张表
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonTable {
    private String tableName;
    private Integer level;
    private List<Column> insert;
    private List<Column> update;
    private List<Column> delete;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Column{
        private String column;
    }
}
