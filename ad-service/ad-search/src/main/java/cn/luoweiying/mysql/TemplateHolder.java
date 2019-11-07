package cn.luoweiying.mysql;

import cn.luoweiying.mysql.constant.OpType;
import cn.luoweiying.mysql.dto.ParseTemplate;
import cn.luoweiying.mysql.dto.TableTemplate;
import cn.luoweiying.mysql.dto.Template;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TemplateHolder {

    private ParseTemplate parseTemplate;
    //在这里注入一个JdbcTemplate，感觉不应该，本来就跟数据库是断开的。
    private JdbcTemplate jdbcTemplate;

    private String SQL_SCHEMA = "select table_schema, table_name, column_name, ordinal_position from information_schema.columns" +
            " where table_schema = ? and table_name = ?";

    @Autowired
    public TemplateHolder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void init() {
        loadJson("template.json");
    }

    //对外提供服务 ， 单独操作一张表
    public TableTemplate getTable(String tableName) {
        return parseTemplate.getMap().get(tableName);
    }

    //加载json文件 , 通过json.parseObject 生成Template对象， 并传给ParseTemplate.parse(template)。
    private void loadJson(String path) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream instream = cl.getResourceAsStream(path);

        try {
            Template template = JSON.parseObject(instream, Charset.defaultCharset(), Template.class);
            this.parseTemplate = ParseTemplate.parse(template);
            loadMeta();
        } catch (IOException e) {
            log.error("fail to parse json file" + e.getMessage());
            throw new RuntimeException("fail to parse json file");
        }

    }

    // 通过数据库查询 表-列序号对应的列名，并对TableTemplate.posMap进行填充，用了jdbc。
    private void loadMeta() {
        for (Map.Entry<String, TableTemplate> entry : parseTemplate.getMap().entrySet()) {
            TableTemplate tableTemplate = entry.getValue();
            List<String> insertFields = tableTemplate.getOpTypeFieldSetMap().get(OpType.ADD);
            List<String> updateFields = tableTemplate.getOpTypeFieldSetMap().get(OpType.UPDATE);
            List<String> deleteFields = tableTemplate.getOpTypeFieldSetMap().get(OpType.DELETE);

            jdbcTemplate.query(SQL_SCHEMA, new Object[]{
                    parseTemplate.getDatabase(), tableTemplate.getTabelName()
            }, (rs, i) -> {
                int pos = rs.getInt("ORDINAL_POSITION");
                String colName = rs.getString("COLUMN_NAME");
//也就是说广告主 很难有扩展性。 系统启动的时候， 被json文件限制了
                //Template表中OpTypeFieldSetMap全部值是否存在此列名 。
                // 不管任何操作，序号对时对应列名的，为什么不全部一次保存，不做判断。？？？
                if ((null != insertFields && insertFields.contains(colName)) ||
                        (null != updateFields && updateFields.contains(colName)) ||
                        (null != deleteFields && deleteFields.contains(colName))) {
                    tableTemplate.getPosMap().put(pos - 1, colName);
                }
                return null;
            });
        }
    }
}
