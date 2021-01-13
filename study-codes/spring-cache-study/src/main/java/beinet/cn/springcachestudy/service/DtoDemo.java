package beinet.cn.springcachestudy.service;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DtoDemo
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/13 14:22
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DtoDemo {
    private int id;
    private Integer age;
    private String name;
    private LocalDateTime birthday;
    private LocalDateTime creationTime;
}
