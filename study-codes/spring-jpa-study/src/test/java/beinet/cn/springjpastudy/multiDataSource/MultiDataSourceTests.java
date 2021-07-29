package beinet.cn.springjpastudy.multiDataSource;

import beinet.cn.springjpastudy.repository.Aaa;
import beinet.cn.springjpastudy.repository.AaaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class MultiDataSourceTests {
    @Autowired
    AaaRepository aaaRepository;

    @Test
    public void test_getData() {
        List<Aaa> masterData = aaaRepository.findAll();
        System.out.println(masterData.get(0));

        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<Aaa> slaveData = aaaRepository.findAllByIdIn(ids);
        System.out.println(slaveData.get(0));
    }
}
