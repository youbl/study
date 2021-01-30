package beinet.cn.demounittestmockmvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

@SpringBootTest
class DemoUnittestMockmvcApplicationTests {


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    Environment env;

    @Test
    void test_http_get() throws Exception {
        // 创建mockMvc对象
        // MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new DbController()).build();
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // 构建一个请求
        RequestBuilder builder = MockMvcRequestBuilders
                .get("/hello?query2=xxxx")              // 要请求的url路径
                .header("x-merchant-id", "12345")   // 添加请求header
                .cookie(new Cookie("", ""))           // 添加cookie，注意：不能用header方法加cookie
                .param("query1", "abcd");           // 添加请求参数，注意：直接注入方法，而不是放在QueryString里
        // 发起请求
        ResultActions result = mockMvc.perform(builder);
        // 避免后面的print里，输出乱码
        result.andReturn().getResponse().setCharacterEncoding("UTF-8");
        // 对响应结果进行断言
        MvcResult mvcResult = result.andExpect(MockMvcResultMatchers.status().isOk())  // 断言：响应status必须是200
                .andExpect(MockMvcResultMatchers.content().string("Hello,abcd12345")) // 断言：响应内容必须完全匹配
                .andDo(MockMvcResultHandlers.print())  // 对结果进行输出
                .andReturn();

        // 获取完整响应内容
        String response = mvcResult.getResponse().getContentAsString();
        // 上面断言去掉，在这里进行断言也可以

        System.out.println("===================================================================");
        // 失败断言
        builder = MockMvcRequestBuilders
                .get("/hello")
                .header("x-merchant-id", "12345")
                .param("query2", "abcd"); // 传递错误参数，应该返回400
        result = mockMvc.perform(builder);
        // 避免后面的print里，输出乱码
        result.andReturn().getResponse().setCharacterEncoding("UTF-8");
        // 断言
        MvcResult realResult = result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(""))
                .andReturn(); // 对结果进行输出

        Assert.isTrue(realResult.getResolvedException() instanceof MissingServletRequestParameterException, "应该是缺少参数的异常");
    }

    @Test
    void test_http_post() throws Exception {
        // 创建mockMvc对象
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // 构建一个请求，包括url、header、参数
        RequestBuilder builder = MockMvcRequestBuilders
                .post("/hello")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"张三\",\"desc\":\"哈哈\",\"sex\":15}")
                .header("x-merchant-id", "12345")
                .param("query1", "abcd");
        // 发起请求
        ResultActions result = mockMvc.perform(builder);
        // 避免后面的print里，输出乱码
        result.andReturn().getResponse().setCharacterEncoding("UTF-8");
        // 断言
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                // 输出的json里，desc的值进行断言，参考 http://goessner.net/articles/JsonPath/
                .andExpect(MockMvcResultMatchers.jsonPath("$.desc").value("哈哈-12345"))
                .andDo(MockMvcResultHandlers.print()); // 对结果进行输出
    }

}
