package beinet.cn.reflectionstudy;

import beinet.cn.reflectionstudy.getMethod.TestClass;
import beinet.cn.reflectionstudy.returnTypeDemo.MethodReturnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReflectionStudyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ReflectionStudyApplication.class, args);
    }

    @Autowired
    private MethodReturnType methodReturnType;

    @Override
    public void run(String... args) throws Exception {
        methodReturnType.runTest();
        //new TestClass().test();
    }

}
