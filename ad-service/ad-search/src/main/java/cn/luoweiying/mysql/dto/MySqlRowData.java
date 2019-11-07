package cn.luoweiying.mysql.dto;

import cn.luoweiying.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//对外投递的对象，  BinlogRowData对象相对复杂
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MySqlRowData {
    private String tableName;
    private String level;
    private OpType opType;
    private List<Map<String, String>> fieldValueMap = new ArrayList<>();
}
