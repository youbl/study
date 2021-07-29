package beinet.cn.springjpastudy.beans;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.sql.DataSource;
import java.util.Map;

@Slf4j
public class MyDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private Environment env;
    private Binder binder;
    //    private DataSource defaultDataSource;
    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases();

    static {
        // 部分数据源要求的配置名不同，这里做兼容
        aliases.addAliases("url", new String[]{"jdbc-url"});
        aliases.addAliases("username", new String[]{"user"});
    }

//    public MyDataSourceRegister(Environment env, DataSource source) {
//        this.env = env;
//        binder = Binder.get(env);
//
//        this.defaultDataSource = source;
//    }

    @Override
    public void setEnvironment(Environment environment) {
        this.env = environment;
        binder = Binder.get(env);
    }

//    @Override
//    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
//        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(annotationMetadata, registry, importBeanNameGenerator);
//    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        Map masterConfig = binder.bind("spring.datasource.master", Map.class).get();
        DataSource masterDateSource = bind(HikariDataSource.class, masterConfig);
        MultiDataSource.addDataSource(MultiDataSource.MASTER, masterDateSource);
        log.info("master数据源添加成功");

        Map slaveConfig = binder.bind("spring.datasource.slave", Map.class).get();
        DataSource slaveDateSource = bind(HikariDataSource.class, slaveConfig);
        MultiDataSource.addDataSource(MultiDataSource.SLAVE, slaveDateSource);
        log.info("slave数据源添加成功");

        GenericBeanDefinition define = new GenericBeanDefinition();
        define.setBeanClass(MultiDataSource.class);
        // 需要注入的参数
        MutablePropertyValues mpv = define.getPropertyValues();
        // 添加默认数据源，避免key不存在的情况没有数据源可用
        mpv.add("defaultTargetDataSource", slaveDateSource);
        // 添加完整数据源
        //mpv.add("targetDataSources", customDataSources);
        // 将该bean注册为datasource，不使用springboot自动生成的datasource

        registry.registerBeanDefinition("datasource", define);
        log.info("注册数据源成功");
    }

    private <T extends DataSource> T bind(Class<T> clazz, Map properties) {
        ConfigurationPropertySource source = new MapConfigurationPropertySource(properties);
        Binder binder = new Binder(new ConfigurationPropertySource[]{source.withAliases(aliases)});
        // 通过类型绑定参数并获得实例对象
        return binder.bind(ConfigurationPropertyName.EMPTY, Bindable.of(clazz)).get();
    }


//    @Bean
//    @ConfigurationProperties("spring.datasource.master")
//    @Primary
//    public DataSource masterDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean
//    @ConfigurationProperties("spring.datasource.slave")
//    public DataSource slaveDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean
//    //@Primary
//    public DataSource multiDataSource(
//            @Qualifier("masterDataSource") DataSource master,
//            @Qualifier("slaveDataSource") DataSource slave
//    ) {
//        Map<Object, Object> target = new HashMap<>();
//        target.put(MultiDataSource.MASTER, master);
//        target.put(MultiDataSource.SLAVE, slave);
//
//        MultiDataSource dataSource = new MultiDataSource();
//        dataSource.setTargetDataSources(target);
//        dataSource.setDefaultTargetDataSource(master);
//        return dataSource;
//    }


//
//    private Class<? extends DataSource> getDataSourceClass(String className) {
//        if (StringUtils.isEmpty(className))
//            return HikariDataSource.class; // springboot 默认值
//        try {
//            return (Class<? extends DataSource>) Class.forName(className);
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException("获取类出错:" + className, e);
//        }
//    }
}
