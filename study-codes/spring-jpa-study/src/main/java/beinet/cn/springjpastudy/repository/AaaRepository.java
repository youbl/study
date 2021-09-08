package beinet.cn.springjpastudy.repository;

import beinet.cn.springjpastudy.beans.MultiDataSource;
import beinet.cn.springjpastudy.beans.MySource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AaaRepository
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/30 9:42
 */
@MySource(MultiDataSource.MASTER)
public interface AaaRepository extends JpaRepository<Aaa, Long>, JpaSpecificationExecutor<Aaa> {
    int deleteByIdIn(List<Long> ids);

    @MySource(MultiDataSource.SLAVE)
    List<Aaa> findAllByIdIn(List<Long> ids);

    @Query(value = "select * from aaa where id in (?1)", nativeQuery = true)
    List<Aaa> findAllByIdIn2(List<Long> ids);

    @Modifying
    @Transactional
    @Query(value = "delete from aaa limit 1;delete from aaa limit 2", nativeQuery = true)
    int deleteAndFind();
}
