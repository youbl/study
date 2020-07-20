package cn.beinet;

import cn.beinet.dto.TestInputDto;
import cn.beinet.dto.TestOutputDto;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        TestInputDto dto = getInputDto();
        System.out.println(dto + " 开始测试");

        TestOutputDto result = new ForTestService(dto).startTest();
        System.out.println("测试完成： " + result);
    }

    static TestInputDto getInputDto() throws IOException {
        TestInputDto ret = new TestInputDto();
        System.out.print("请输入测试地址，默认值 https://www.baidu.com/: ");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ret.setUrl(reader.readLine());
        if (StringUtils.isEmpty(ret.getUrl())) {
            ret.setUrl("https://www.baidu.com/");
        } else {
            ret.setUrl(ret.getUrl().trim());
            Pattern reg = Pattern.compile("^(?i)https?://");
            Matcher m1 = reg.matcher(ret.getUrl());
            if (!m1.find()) {
                ret.setUrl("http://" + ret.getUrl());
            }
        }

        String strRequestTime;
        do {
            System.out.print("请输入总请求次数，默认值 100: ");
            strRequestTime = reader.readLine();
            if (StringUtils.isEmpty(strRequestTime))
                strRequestTime = "100";
        } while (!StringUtils.isNumeric(strRequestTime));
        ret.setRequestTime(Long.parseLong(strRequestTime));

        String strConcurrencyNum;
        do {
            System.out.print("请输入并发数，默认值 10: ");
            strConcurrencyNum = reader.readLine();
            if (StringUtils.isEmpty(strConcurrencyNum))
                strConcurrencyNum = "10";
        } while (!StringUtils.isNumeric(strConcurrencyNum));
        ret.setConcurrencyNum(Long.parseLong(strConcurrencyNum));

        reader.close();

        return ret;
    }
}
