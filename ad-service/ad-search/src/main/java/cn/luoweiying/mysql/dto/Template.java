package cn.luoweiying.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
//解析整个 json数据
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Template {
    private String database;
    private List<JsonTable> tableList;

}
