package beinet.cn.mapstructdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/8 20:22
 */
@Data
@Builder
@NoArgsConstructor      // MapStruct必须要有无参构造函数
@AllArgsConstructor     // Builder注解必须要有全参构造函数
public class Aaa {
    private int id;
    private Integer age;
    private String name;
    private LocalDateTime birthday;

    private Long num;

    private String description;
}
