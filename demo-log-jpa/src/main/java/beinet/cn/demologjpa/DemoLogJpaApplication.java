package beinet.cn.demologjpa;

import beinet.cn.demologjpa.jpa.AaaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoLogJpaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoLogJpaApplication.class, args);
    }

    @Autowired
    AaaRepository repository;

    @Override
    public void run(String... args) throws Exception {
        repository.find1(1, 222);
        repository.findById(1L);
    }
}
