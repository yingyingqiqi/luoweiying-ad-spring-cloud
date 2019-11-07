package cn.luoweiying.mysql.dto;

import cn.luoweiying.mysql.constant.OpType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//binlog日志使用，binlog日志只保存了 序号和前后结果， 没有列名
//增删改查所对应的列名，感觉把JsonTable实体类再次解析了,相比就是增加一个序号
// 序号一一对应列名
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableTemplate {
    private String tabelName;
    private String level;
    //操作 对应  列名
    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>();
    //列序号  对应 列名
    private Map<Integer, String> posMap = new HashMap<>();

}
