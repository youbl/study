package beinet.cn.algorithmdemo.controller;

import beinet.cn.algorithmdemo.sort.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class HomeController {
    private final List<Sort> sortMethodList;

    public HomeController(List<Sort> sortMethodList) {
        this.sortMethodList = sortMethodList;
    }

    @GetMapping("/")
    public List<SortItem[]> index(@RequestParam(required = false) String ids) {
        SortItem[] arr = toArr(ids);

        List<SortItem[]> results = new ArrayList<>(sortMethodList.size() + 1);
        results.add(arr);

        for (Sort sort : sortMethodList) {
            SortItem[] arrClone = arr.clone();
            sort.sort(arrClone);
            results.add(arrClone);
        }
        return results;
    }

    private SortItem[] toArr(String ids) {
        List<SortItem> arr = new ArrayList<>();
        if (StringUtils.hasText(ids)) {
            int idx = 0;
            for (String item : ids.split(",")) {
                String tmp = item.trim();
                if (tmp.length() <= 0) {
                    continue;
                }
                SortItem sortItem = SortItem.builder()
                        .num(Integer.parseInt(item))
                        .originIdx(idx)
                        .build();
                arr.add(sortItem);
                idx++;
            }
        } else {
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                SortItem sortItem = SortItem.builder()
                        .num(random.nextInt() % 100)
                        .originIdx(i)
                        .build();
                arr.add(sortItem);
            }
        }
        return arr.toArray(new SortItem[0]);
        //return arr.stream().mapToInt(Integer::valueOf).toArray(); 转成int数组
        // return arr.toArray(new Integer[0]);
    }
}
