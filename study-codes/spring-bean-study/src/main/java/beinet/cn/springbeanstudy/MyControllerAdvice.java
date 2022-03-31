package beinet.cn.springbeanstudy;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/30 13:45
 */
@ControllerAdvice
@Order(10000)
public class MyControllerAdvice {
    @InitBinder
    public void setAllowedFidles(WebDataBinder dataBinder) {
        String[] abd = new String[]{"class.*", "Class.*", "*.class.*", "*.Class.*", "id"};
        dataBinder.setDisallowedFields(abd);
    }
}
