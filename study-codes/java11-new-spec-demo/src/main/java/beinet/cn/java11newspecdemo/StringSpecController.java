package beinet.cn.java11newspecdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * 演示java11里比java8，新增的String方法
 *
 * @author youbl
 * @since 2023/6/7 14:07
 */
@RestController
public class StringSpecController {
    @GetMapping("String")
    public Map<String, String> newMethod() {
        // LinkedHashMap会保持put插入的顺序，而HashMap是无序的
        Map<String, String> ret = new LinkedHashMap<>();

        String repeatStr = "hello";
        ret.put("\"" + repeatStr + "\".repeat(3)", repeatStr.repeat(3)); // 输出 hellohellohello

        // 老的trim方法不能移除全角空格，而java11新增的strip可以移除
        String stripStr = "  　hello  world　  ";
        ret.put("\"" + stripStr + "\".trim()", stripStr.trim());
        ret.put("\"" + stripStr + "\".strip()", stripStr.strip());              // 移除前面和后面的空格
        ret.put("\"" + stripStr + "\".stripLeading()", stripStr.stripLeading());// 移除前面的空格
        ret.put("\"" + stripStr + "\".stripTrailing()", stripStr.stripTrailing());// 移除后面的空格

        // isEmpty只判断""， isBlank还判断是否全部是空格（含全角空格）
        String isBlankStr = "　  ";
        ret.put("\"" + isBlankStr + "\".isBlank()", String.valueOf(isBlankStr.isBlank()));

        // lines根据 \r 或 \n 或 \r\n 进行拆分，如果有多个\r 或 \n 或 \r\n，会拆分出空字符串到结果数组里
        String linesStr = "hello \n world\rbeinet.cn\r\n123\n\n456\r\r789\r\n\r000\n\r";
        // 输出结果是 【hello 】,【 world】,【beinet.cn】,【123】,【】,【456】,【】,【789】,【】,【000】,【】
        ret.put("\"" + linesStr + "\".lines()", combineArr(linesStr.lines()));
        
        return ret;
    }

    private String combineArr(Stream<String> arr) {
        final String[] ret = {""};
        arr.forEach(str -> {
            if (!ret[0].isEmpty())
                ret[0] += ",";
            ret[0] += "【" + str + "】";
        });
        return ret[0];
    }
}
