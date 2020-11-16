package beinet.cn.demounittestjpa.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    @CreatedDate
    private LocalDateTime creationTime;
}
