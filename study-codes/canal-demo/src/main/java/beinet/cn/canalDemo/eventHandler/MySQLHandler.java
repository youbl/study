package beinet.cn.canalDemo.eventHandler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.*;

import java.time.LocalDateTime;

@CanalEventListener
public class MySQLHandler {
//    @InsertListenPoint
//    public void onInsertEvent(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
//        outputMsg(eventType, rowData, "onInsertEvent");
//    }
//
//    @UpdateListenPoint
//    public void onUpdateEvent(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
//        outputMsg(eventType, rowData, "onUpdateEvent");
//    }
//
//    @DeleteListenPoint
//    public void onDeleteEvent(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
//        outputMsg(eventType, rowData, "onDeleteEvent");
//    }

    @ListenPoint
//(destination = "example", schema = "canal-test", table = {"t_user", "test_table"}, eventType = CanalEntry.EventType.UPDATE)
    public void onAllEvent(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        outputMsg(eventType, rowData, "onAllEvent");
    }

    private void outputMsg(CanalEntry.EventType eventType, CanalEntry.RowData rowData, String title) {
        System.out.println(LocalDateTime.now() + " " + title + " 收到事件:" + eventType);
        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
            System.out.println(title + ":BEFORE  " + column.getName() + ":" + column.getValue());
        }
        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            System.out.println(title + ":AFTER   " + column.getName() + ":" + column.getValue());
        }
    }
}
