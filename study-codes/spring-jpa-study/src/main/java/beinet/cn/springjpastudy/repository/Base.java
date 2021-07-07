package beinet.cn.springjpastudy.repository;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Aaa
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/30 9:42
 */
@MappedSuperclass
@Data
public class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long restId;
    private int dishhour;
    private long dishId;
    private int num;
    @Column(insertable = false, updatable = false)
    private LocalDateTime creationTime;
    
    @Column(insertable = false, updatable = false)
    private String name;
}
