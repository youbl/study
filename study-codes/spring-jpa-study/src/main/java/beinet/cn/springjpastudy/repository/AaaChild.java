package beinet.cn.springjpastudy.repository;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Aaa
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/30 9:42
 */
@Entity
@Data
public class AaaChild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long aaaId;
    private String childName;
    @Column(updatable = false)
    private LocalDateTime creationTime;
}
