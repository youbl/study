package beinet.cn.springjpastudy.beans;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(MyDataSourceRegister.class)
public class MyDataSourceAutoConfig {
}
