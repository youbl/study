package beinet.cn.mapstructdemo.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/8 20:23
 */
@Data
public class AaaDto {
    private int id;
    private Integer age;
    private String name;
    private LocalDateTime birthday;

    private Long Num;

    private String desc;
}
