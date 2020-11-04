package beinet.cn.demomongounittest;

import beinet.cn.demomongounittest.entity.Users;
import beinet.cn.demomongounittest.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class DemoMongoUnittestApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoMongoUnittestApplication.class, args);
    }

    @Autowired
    UsersRepository repository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("run方法启动...");

        long cnt = repository.count();
        System.out.println("总记录数： " + cnt);

        Users item = repository.findTopByNameOrderByIdDesc("张三");
        if (item == null) {
            item = Users.builder()
                    .id(12L)
                    .name("张三")
                    .birthday(LocalDateTime.of(2001, 11, 30, 23, 59))
                    .gender(2)
                    .desc("插入")
                    .build();
        } else {
            System.out.println("更新前: " + item);
            item.setDesc(item.getDesc() + "-更新");
        }
        item = repository.save(item);
        System.out.println("保存后: " + item);

    }
}
