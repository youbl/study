package beinet.cn.demologmybatis;

import beinet.cn.demologmybatis.dal.ProjectsMapper;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"beinet.cn.demologmybatis.dal"})
@Slf4j
public class DemoLogMybatisApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoLogMybatisApplication.class, args);
    }

    @Autowired
    ProjectsMapper projectsMapper;

    @Override
    public void run(String... args) throws Exception {
        log.info("开始读库");
        projectsMapper.getByName("");
        log.info("结束");
    }
}
