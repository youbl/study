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
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Aaa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long restId;
    private int dishhour;
    private long dishId;
    private int num;
    //    @Column(updatable = false)
    // 在主方法上添加  @EnableJpaAuditing 这个CreateDate就会自动赋值
    @CreatedDate
    private LocalDateTime creationTime;

    // fetch 默认为LAZY，会报错：failed to lazily initialize a collection of role: beinet.cn.springjpastudy.repository.Aaa.bbb
    @OneToMany(fetch = FetchType.EAGER)
    // name是AaaChild表的字段，referencedColumnName是当前表的字段
    @JoinColumn(name = "aaaId", referencedColumnName = "id", insertable = false, updatable = false)
    private List<AaaChild> bbb; // OneToMany 必须是数组
}
