package beinet.cn.springjpastudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * AaaRepository
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/30 9:42
 */
public interface Child1Repository extends JpaRepository<Child1, Long>, JpaSpecificationExecutor<Aaa> {
    int deleteByIdIn(List<Long> ids);

    List<Child1> findAllByIdIn(List<Long> ids);

    @Query(value = "select * from Child1 where id in (?1)", nativeQuery = true)
    List<Child1> findAllByIdIn2(List<Long> ids);

}
