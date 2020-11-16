package beinet.cn.demounittestmockito;


import org.springframework.stereotype.Service;

/**
 * 这是一个业务类
 */
@Service
public class BusinessService {

    /**
     * 请求并返回百度的html
     *
     * @return html
     */
    public String requestBaiduHtml() {
        return "我是百度";
    }

    /**
     * 请求并返回新浪的html
     *
     * @return html
     */
    public String requestSinaHtml() {
        return "我是新浪";
    }


    /**
     * 根据参数处理后返回
     *
     * @param para 参数
     * @return 返回
     */
    public String requestByPara(String para) {
        if (para == null)
            para = "isNULL";
        else if (para.length() == 0)
            para = "isEMPTY";
        return "我收到参数:" + para;
    }

    /**
     * 外部mock抛异常用
     *
     * @param obj 参数
     */
    public void throwExp(Object obj) {
        System.out.println(obj);
        throw new IllegalArgumentException("抛个异常1");
    }

    /**
     * 外部mock抛异常用
     *
     * @param obj 参数
     */
    public Object throwExpWithRet1(Object obj) {
        System.out.println(obj);
        throw new IllegalArgumentException("抛个异常2");
    }

    /**
     * 外部mock抛异常用
     *
     * @param obj 参数
     */
    public Object throwExpWithRet2(Object obj) {
        System.out.println(obj);
        throw new IllegalArgumentException("抛个异常3");
    }
}
