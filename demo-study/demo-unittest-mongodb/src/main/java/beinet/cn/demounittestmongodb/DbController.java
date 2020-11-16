package beinet.cn.demounittestmongodb;

import beinet.cn.demounittestmongodb.entity.Users;
import beinet.cn.demounittestmongodb.repository.UsersRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class DbController {
    private final UsersRepository repository;

    public DbController(UsersRepository repository) {
        this.repository = repository;
    }

    @GetMapping("mongo")
    public Users addMongo() {
        int id = 123;
        LocalDateTime now = LocalDateTime.now();
        Users item = Users.builder()
                .id(id)
                .name("张三" + id)
                .ctime(now)
                .birthday(now.minusMinutes(id - 3))
                .gender(2)
                .desc("插入")
                .build();
        repository.insert(item);
        return item;
    }

    @GetMapping("index")
    public String index() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) + " " + this.getClass().getName();
    }
}
