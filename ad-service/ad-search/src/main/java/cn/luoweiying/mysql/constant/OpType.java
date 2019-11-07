package cn.luoweiying.mysql.constant;

import com.github.shyiko.mysql.binlog.event.EventType;

public enum  OpType {
    ADD,
    UPDATE,
    DELETE,
    OTHER;

    //对binlog日志解析对象 进行类型转换   不同数据库 不同
    public static OpType to(EventType eventType) {
        switch (eventType) {
            case EXT_WRITE_ROWS:return ADD;
            case EXT_UPDATE_ROWS:return UPDATE;
            case EXT_DELETE_ROWS: return DELETE;
            default: return OTHER;
        }
    }
}

