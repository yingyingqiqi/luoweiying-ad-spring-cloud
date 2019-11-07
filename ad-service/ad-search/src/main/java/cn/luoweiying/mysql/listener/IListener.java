package cn.luoweiying.mysql.listener;

import cn.luoweiying.mysql.dto.BinlogRowData;
//不同业务，对binglogRowData 做不同处理    register向aggregationListener.listenerMap注册
public interface IListener {
    public void register();

    public void OnEvent(BinlogRowData binlogRowData);

}
