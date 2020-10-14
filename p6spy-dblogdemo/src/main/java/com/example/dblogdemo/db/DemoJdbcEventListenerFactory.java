package com.example.dblogdemo.db;

import com.p6spy.engine.common.PreparedStatementInformation;
import com.p6spy.engine.common.StatementInformation;
import com.p6spy.engine.event.CompoundJdbcEventListener;
import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.spy.DefaultJdbcEventListenerFactory;
import com.p6spy.engine.spy.JdbcEventListenerFactory;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

// 在resources/META-INF/services/com.p6spy.engine.spy.JdbcEventListenerFactory 里加载
public class DemoJdbcEventListenerFactory implements JdbcEventListenerFactory {
    private CompoundJdbcEventListener compoundJdbcEventListener;

    public DemoJdbcEventListenerFactory() {
        JdbcEventListener listener = new DefaultJdbcEventListenerFactory().createJdbcEventListener();
        if (listener instanceof CompoundJdbcEventListener) {
            compoundJdbcEventListener = (CompoundJdbcEventListener) listener;
        } else {
            compoundJdbcEventListener = new CompoundJdbcEventListener();
            compoundJdbcEventListener.addListender(listener);
        }

        compoundJdbcEventListener.addListender(new MyJdbcEventListener());
    }

    @Override
    public JdbcEventListener createJdbcEventListener() {
        return compoundJdbcEventListener;
    }

    @Slf4j
    public static class MyJdbcEventListener extends JdbcEventListener {
        @Override
        public void onAfterExecuteBatch(StatementInformation statementInformation, long timeElapsedNanos, int[] updateCounts, SQLException e) {
            System.out.println(updateCounts.toString());
        }

        @Override

        public void onAfterExecuteUpdate(PreparedStatementInformation statementInformation, long timeElapsedNanos, int rowCount, SQLException e) {
            System.out.println(rowCount);
        }

        @Override
        public void onAfterExecuteUpdate(StatementInformation statementInformation, long timeElapsedNanos, String sql, int rowCount, SQLException e) {
            System.out.println(rowCount);
        }
    }
}
