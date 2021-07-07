package beinet.cn.springjpastudy;

import beinet.cn.springjpastudy.repository.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ChildController {
    private final Map<String, BaseRepository> baseRepositoryList;

    public ChildController(List<BaseRepository> baseRepositoryList) {
        this.baseRepositoryList = new HashMap<>();
        for (BaseRepository item : baseRepositoryList) {
            this.baseRepositoryList.put(item.getName(), item);
        }
    }

    @GetMapping("child1")
    public List<Child1> getAll1() throws InstantiationException, IllegalAccessException {
        BaseRepository child1Repository = this.baseRepositoryList.get("child1");
        Base child1 = (Base) child1Repository.getEntityType().newInstance();
        child1.setDishhour(11);
        child1.setNum(22);

        child1Repository.save(child1);
        return child1Repository.findAll();
    }


    @GetMapping("child2")
    public List<Child2> getAll2() throws InstantiationException, IllegalAccessException {
        BaseRepository child2Repository = this.baseRepositoryList.get("child2");
        Base child2 = (Base) child2Repository.getEntityType().newInstance();
        child2.setDishhour(221);
        child2.setNum(2222);

        child2Repository.save(child2);
        return child2Repository.findAll();
    }
}
