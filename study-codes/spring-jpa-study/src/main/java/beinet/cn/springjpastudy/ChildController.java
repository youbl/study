package beinet.cn.springjpastudy;

import beinet.cn.springjpastudy.repository.Child1;
import beinet.cn.springjpastudy.repository.Child1Repository;
import beinet.cn.springjpastudy.repository.Child2;
import beinet.cn.springjpastudy.repository.Child2Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChildController {
    private final Child1Repository child1Repository;
    private final Child2Repository child2Repository;

    public ChildController(Child1Repository child1Repository, Child2Repository child2Repository) {
        this.child1Repository = child1Repository;
        this.child2Repository = child2Repository;
    }

    @GetMapping("child1")
    public List<Child1> getAll1() {
        Child1 child1 = new Child1();
        child1.setDishhour(11);
        child1.setNum(22);

        child1Repository.save(child1);
        return child1Repository.findAll();
    }


    @GetMapping("child2")
    public List<Child2> getAll2() {
        Child2 child2 = new Child2();
        child2.setDishhour(221);
        child2.setNum(2222);

        child2Repository.save(child2);
        return child2Repository.findAll();
    }
}
