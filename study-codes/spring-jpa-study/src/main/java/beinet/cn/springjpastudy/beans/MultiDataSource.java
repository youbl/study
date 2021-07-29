package beinet.cn.springjpastudy.beans;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源选择类
 */
@Slf4j
public class MultiDataSource extends AbstractRoutingDataSource {
    public final static String MASTER = "master";
    public final static String SLAVE = "slave";
    private final static Map<Object, Object> targetDataSources = new HashMap<>();

    public MultiDataSource() {
        setTargetDataSources(targetDataSources);
    }

    // 使用ThreadLocal避免线程安全问题
    private final static ThreadLocal<String> currentDataSource = new ThreadLocal<>();

    /**
     * 添加数据源
     *
     * @param key
     * @param dataSource
     */
    public static void addDataSource(String key, DataSource dataSource) {
        targetDataSources.put(key, dataSource);
    }
    
    /**
     * 切换到主库
     */
    public static void useMaster() {
        currentDataSource.set(MASTER);
        log.info("切换到主库");
    }

    /**
     * 切换到从库
     */
    public static void useSlave() {
        currentDataSource.set(SLAVE);
        log.info("切换到从库");
    }

    /**
     * 清理设置
     */
    public static void clear() {
        currentDataSource.set("");
    }

    /**
     * 返回当前使用的库
     *
     * @return 当前库
     */
    public static String getCurrent() {
        String usingSource = currentDataSource.get();
        if (StringUtils.isEmpty(usingSource))
            return SLAVE;
        return usingSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String usingSource = getCurrent();
        log.info("当前使用的数据源:{}", usingSource);
        return usingSource;
    }

}
