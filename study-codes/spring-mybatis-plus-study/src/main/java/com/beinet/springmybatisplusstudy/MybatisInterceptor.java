package com.beinet.springmybatisplusstudy;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.TextSqlNode;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


@Intercepts({
        // 拦截插入和更新
        @Signature(type = Executor.class, method = "update",
                args = {MappedStatement.class, Object.class}),
        // 拦截查询
        @Signature(type = Executor.class, method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
@Component
public class MybatisInterceptor implements Interceptor {

    Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public Object intercept(Invocation invocation) throws IllegalAccessException, InvocationTargetException {
        Executor executor = (Executor) invocation.getTarget();
        /* * Executor 的 update 方法里面有一个参数 MappedStatement，它是包含了 sql 语句的，所以我获取这个对象
         * * 以下是伪代码，思路：
         * * 1 通过反射从 Executor 对象中获取 MappedStatement 对象
         * * 2 从 MappedStatement 对象中获取 SqlSource 对象
         * * 3 然后从 SqlSource 对象中获取获取 BoundSql 对象
         * * 4 最后通过 BoundSql#getSql 方法获取 sql */
//        MappedStatement mappedStatement = ReflectUtil.getMethodField(executor, MappedStatement.class);
//        SqlSource sqlSource = ReflectUtil.getField(mappedStatement, SqlSource.class);
//        BoundSql boundSql = sqlSource.getBoundSql(args);

        fillField(invocation);
        return invocation.proceed();
    }


    private void fillField(Invocation invocation) {
        Object[] args = invocation.getArgs();
        SqlCommandType sqlCommandType = null;

        Object commandTypeArg = args[0];
        Object parameter = args.length > 1 ? args[1] : null;

        log.info("参数类型:{}", commandTypeArg.getClass().getName());
        if (commandTypeArg instanceof MappedStatement) {
            MappedStatement mappedStatement = (MappedStatement) commandTypeArg;
            sqlCommandType = mappedStatement.getSqlCommandType();

            Configuration configuration = mappedStatement.getConfiguration();
            SqlSource sqlSource = mappedStatement.getSqlSource();
            BoundSql boundSql = sqlSource.getBoundSql(parameter);
            //Object[] params = getParams(configuration, boundSql);

            String sql = getRealSql(configuration, boundSql);
            // 这个拿到的是带问号的sql
            //String sql = sqlSource.getBoundSql(parameter).getSql();
            //String sql = getSql(sqlSource);

            log.info("操作类型：{} {}", sqlCommandType, sql);
            // if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE)
        }

        for (int i = 1, j = args.length; i < j; i++) {
            Object arg = args[i];
            if (arg == null) {
                log.info("{} 值为null", i);
                continue;
            }
            for (Field field : arg.getClass().getDeclaredFields()) {
                log.info("{} 类型:{} 字段名: {} 值:{}",
                        i,
                        arg.getClass().getName(),
                        field.getName(),
                        getProperty(arg, field));
//                if (arg instanceof HashMap) {
//                    HashMap map = (HashMap) arg;
//                    for (Object item : map.keySet()) {
//                        log.info(item + ":" + map.get(item));
//                    }
//                }

                if (sqlCommandType == SqlCommandType.INSERT) {
                    switch (field.getName()) {
                        case "email":
                            setProperty(arg, field, "123@fdas.com");
                            break;
                        case "dateUpdate":
                            setProperty(arg, field, new Date());
                            break;
                    }
                } else if (sqlCommandType == SqlCommandType.UPDATE) {
                    switch (field.getName()) {
                        case "dateUpdate":
                            setProperty(arg, field, new Date());
                            break;
                    }
                }
            }
        }
    }

    private static Object[] getParams(Configuration configuration, BoundSql boundSql) {
        ArrayList<Object> list = new ArrayList<>();
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (!parameterMappings.isEmpty() && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                list.add(parameterObject);
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        list.add(obj);
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        list.add(obj);
                    } else {
                        list.add("缺失");
                    }//打印出缺失，提醒该参数缺失并防止错位
                }
            }
        }
        return list.toArray();
    }

    private static String getRealSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        //替换空格、换行、tab缩进等
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }

    private static String getParameterValue(Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "null";
            }
        }
        return value.replace("$", "\\$");
    }

    private String getSql(SqlSource sqlSource) {
        try {
            DynamicSqlSource dynamicSqlSource = (DynamicSqlSource) sqlSource;
            //反射获取 TextSqlNode 对象
            Field sqlNodeField = dynamicSqlSource.getClass().getDeclaredField("rootSqlNode");
            sqlNodeField.setAccessible(true);
            Object tmpSqlNode = sqlNodeField.get(dynamicSqlSource);
            if (tmpSqlNode instanceof TextSqlNode) {
                TextSqlNode rootSqlNode = (TextSqlNode) tmpSqlNode;
                //反射获取原生sql
                Field textField = rootSqlNode.getClass().getDeclaredField("text");
                textField.setAccessible(true);
                String sql = String.valueOf(textField.get(rootSqlNode));
                return sql;
            }
            if (tmpSqlNode instanceof MixedSqlNode) {
                MixedSqlNode rootSqlNode = (MixedSqlNode) tmpSqlNode;
            }
            return "";
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    private void setProperty(Object obj, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object getProperty(Object obj, Field field) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public Object plugin(Object o) {
//        return Plugin.wrap(o, this);
//    }
//
//
//    @Override
//    public void setProperties(Properties properties) {
//        System.out.println("abc");
//    }
}

