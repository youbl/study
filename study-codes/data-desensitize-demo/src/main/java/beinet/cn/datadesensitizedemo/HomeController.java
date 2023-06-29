package beinet.cn.datadesensitizedemo;

import beinet.cn.datadesensitizedemo.tests.UserItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/30 11:52
 */
@RestController
public class HomeController {
    @GetMapping("")
    public String index() {
        return this.getClass().getName() + " " + LocalDateTime.now();
    }


    @GetMapping("one")
    public UserItem getOne() {
        return new UserItem()
                .setAddr("北京市长安大街365号")
                .setEmail("youbl@126.com")
                .setPhone("13012345678")
                .setUsername("游北亮");
    }

    @GetMapping("arr")
    public List<UserItem> getArr() {
        var arr = new ArrayList<UserItem>();
        var item = new UserItem()
                .setAddr("北京市长安大街365号")
                .setEmail("youbl@126.com")
                .setPhone("13012345678")
                .setUsername("游北亮");
        arr.add(item);
        item = new UserItem()
                .setAddr("福建省福州市五四北路365号")
                .setEmail("youbl@126.com")
                .setPhone("18912345678")
                .setUsername("游北亮2");
        arr.add(item);
        return arr;
    }
}
