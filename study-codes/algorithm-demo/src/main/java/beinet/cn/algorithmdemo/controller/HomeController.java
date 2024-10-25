package beinet.cn.algorithmdemo.controller;

import beinet.cn.algorithmdemo.sort.DirectInsert;
import beinet.cn.algorithmdemo.sort.Sort;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@Api(tags = "swagger里显示的接口类说明")
public class HomeController {
    private final List<Sort> sortMethodList;

    public HomeController(List<Sort> sortMethodList) {
        this.sortMethodList = sortMethodList;
    }

    @GetMapping(value = "/", produces = "text/plain")
    @ApiOperation(value = "swagger里显示的接口方法说明", notes = "默认的GET方法，显示一些请求信息")
    public String index(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getMethod())
                .append(" ")
                .append(request.getRequestURL())
                .append("\n");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            sb.append("\n")
                    .append(header)
                    .append(": ")
                    .append(request.getHeader(header));
        }

        return sb.toString();
    }

    @ApiOperation(value = "默认的POST方法，显示一些请求信息")
    @PostMapping(value = "/", produces = "text/plain")
    public String indexPost(HttpServletRequest request) {
        return index(request);
    }

    @ApiOperation(value = "默认的DELETE方法，显示一些请求信息")
    @DeleteMapping(value = "/", produces = "text/plain")
    public String indexDelete(HttpServletRequest request) {
        return index(request);
    }

    @ApiOperation(value = "默认的PUT方法，显示一些请求信息")
    @PutMapping(value = "/", produces = "text/plain")
    public String indexPut(HttpServletRequest request) {
        return index(request);
    }

    @ApiOperation(value = "对数据排序后返回，第一行为源数组及长度，后续行为排序后数据，及循环次数")
    @GetMapping("/sort")
    public List<Result> indexSort(@RequestParam(required = false) String ids) {
        SortItem[] arr = toArr(ids);

        List<Result> results = new ArrayList<>(sortMethodList.size() + 1);
        results.add(toResult(arr, String.valueOf(arr.length)));
        results.get(0).setSorted(false);

        List<String> use2Compare = null; // 用于校验排序结果
        for (Sort sort : sortMethodList) {
            SortItem[] arrClone = arr.clone();
            sort.sort(arrClone);
            results.add(toResult(arrClone, sort.getLoopCount() + ":" + sort.getName()));

            if (sort instanceof DirectInsert) {
                use2Compare = results.get(results.size() - 1).getArr();
            }
        }

        validSortResult(results, use2Compare);
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
            // 增加2个相同的数据，用于验证稳定性
            arr.add(SortItem.builder()
                    .num(11)
                    .originIdx(0)
                    .build());
            for (int i = 0; i < 10; i++) {
                SortItem sortItem = SortItem.builder()
                        .num(random.nextInt() % 100)
                        .originIdx(i + 1)
                        .build();
                arr.add(sortItem);
            }
            arr.add(SortItem.builder()
                    .num(11)
                    .originIdx(11)
                    .build());
        }
        return arr.toArray(new SortItem[0]);
        //return arr.stream().mapToInt(Integer::valueOf).toArray(); 转成int数组
        // return arr.toArray(new Integer[0]);
    }

    private List<String> toResult(SortItem[] arr) {
        return Arrays.stream(arr).map(item -> item.getNum() + ":" + item.getOriginIdx()).collect(Collectors.toList());
    }

    private Result toResult(SortItem[] arr, String str) {
        Result ret = new Result();
        ret.setArr(toResult(arr));
        ret.setLoop(str);
        return ret;
    }

    private void validSortResult(List<Result> results, List<String> use2Compare) {
        for (Result result : results) {
            if (!result.sorted || result == use2Compare)
                continue;

            for (int i = 0, j = use2Compare.size(); i < j; i++) {
                String com1 = use2Compare.get(i);
                String com2 = result.getArr().get(i);
                com1 = com1.split(":")[0];
                com2 = com2.split(":")[0];
                if (!com1.equals(com2)) {
                    result.loop += "===排序结果有误";
                    throw new RuntimeException("排序结果有误:" + result.loop);
                    // break;
                }
            }
        }
    }

    @Data
    @ApiModel(description = "swagger里显示的这个类的说明")
    public static class Result {
        @ApiModelProperty(value = "swagger里显示的这个字段的说明，排序结果数据")
        private List<String> arr;
        @ApiModelProperty(value = "循环次数")
        private String loop;

        @JsonIgnore
        private boolean sorted = true;
    }
}
