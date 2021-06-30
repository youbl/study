package beinet.cn.springbeanstudy.interfaceMulti;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * InterfaceTestAutoConfiguration
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/28 14:29
 */
@Configuration
public class InterfaceTestAutoConfiguration {
    @Bean
    public Implment1 createImplment1() {
        return new Implment1();
    }

    @Bean
    public Implment12 createImplment12() {
        return new Implment12();
    }

    @Bean
    public Implment13 createImplment13() {
        return new Implment13();
    }

    @Bean
    public Implment2 createImplment2() {
        return new Implment2();
    }

    @Bean
    public ImplmentAll createImplmentAll() {
        return new ImplmentAll();
    }

    /**
     * @param implment1List 该数组包含2项： Implment1 和 ImplmentAll
     * @param implment2List 该数组包含2项： Implment2 和 ImplmentAll
     * @return
     */
    @Bean
    public UsingAll createUsingAll(List<Interface1> implment1List, List<Interface2> implment2List) {
        return new UsingAll(implment1List, implment2List);
    }
}
