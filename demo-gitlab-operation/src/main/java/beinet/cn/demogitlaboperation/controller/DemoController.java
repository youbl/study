package beinet.cn.demogitlaboperation.controller;

import beinet.cn.demogitlaboperation.feign.GitlabClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * DemoController
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/11 9:51
 */
@RestController
public class DemoController {
    @Autowired
    GitlabClient gitlabClient;

    @GetMapping("demo")
    public String demo(HttpServletRequest request) throws UnsupportedEncodingException {

        //        Enumeration<String> arr = request.getHeaders("aaa");
//        while (arr.hasMoreElements()) {
//            System.out.println(arr.nextElement());
//        }

//        System.out.println("getParameter: " + request.getParameter("aaa"));
//
//        String[] values = request.getParameterValues("aaa");
//        System.out.println("getParameterValues[" + values.length + "]: ");
//        for (String it : values) {
//            System.out.println("===" + it + "===");
//        }

        return "";
    }
}
