package beinet.cn.lombokdemo.demos;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/20 13:30
 */
public class BugDemo {
    /*
    BUG1：
    如果属性添加了 @Accessor(chain = true)注解,
    会把所有的 setXxx 方法，添加this作为返回值，方便链式赋值。
    但是，类似easyexcel这种库，加了这个注解的实体类，导出会不正常。
    原因：
    easyexcel底层使用的是cglib来做反射工具包的：
    // com.alibaba.excel.read.listener.ModelBuildEventListener 类的第130行
    BeanMap.create(resultModel).putAll(map);

    // 最底层的是cglib的BeanMap的这个方法调用
    abstract public Object put(Object bean, Object key, Object value);

    但是cglib使用的是Java的rt.jar里面的一个Introspector这个类的方法：
Introspector.java 第520行
if (int.class.equals(argTypes[0]) && name.startsWith(GET_PREFIX)) {
   pd = new IndexedPropertyDescriptor(this.beanClass, name.substring(3), null, null, method, null);
   //下面这行判断，只获取返回值是void类型的setxxxx方法
 } else if (void.class.equals(resultType) && name.startsWith(SET_PREFIX)) {
    // Simple setter
    pd = new PropertyDescriptor(this.beanClass, name.substring(3), null, method);
    if (throwsException(method, PropertyVetoException.class)) {
       pd.setConstrained(true);
    }
}
    这里是获取返回void类型的set方法，会导致获取不到。
    * */

    // BUG2:  https://mp.weixin.qq.com/s/wu9EQ8xc_txaUVkM5_efIg
}
