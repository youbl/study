package beinet.cn.springjpastudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AaaRepository
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/30 9:42
 */
public interface AaaRepository extends JpaRepository<Aaa, Long> {
    int deleteByIdIn(List<Long> ids);
}
