package beinet.cn.springjpastudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * AaaRepository
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/30 9:42
 */
@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    int deleteByIdIn(List<Long> ids);

    List<Child1> findAllByIdIn(List<Long> ids);

    @Query(value = "select * from #{#entityName} where id in (?1)", nativeQuery = true)
    List<Child1> findAllByIdIn2(List<Long> ids);

    Class getEntityType();

    String getName();
}
