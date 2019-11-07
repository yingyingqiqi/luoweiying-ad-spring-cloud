package cn.luoweiying.mysql.sender;

import cn.luoweiying.mysql.dto.MySqlRowData;

//将MySqlRowData对象投递出去 的接口
public interface ISender {
    void sender(MySqlRowData mySqlRowData);
}
