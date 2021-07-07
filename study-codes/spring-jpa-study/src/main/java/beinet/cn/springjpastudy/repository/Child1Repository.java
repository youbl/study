package beinet.cn.springjpastudy.repository;

/**
 * AaaRepository
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/30 9:42
 */
public interface Child1Repository extends BaseRepository<Child1> {
    @Override
    default Class getEntityType() {
        return Child1.class;
    }

    @Override
    default String getName() {
        return "child1";
    }
}
