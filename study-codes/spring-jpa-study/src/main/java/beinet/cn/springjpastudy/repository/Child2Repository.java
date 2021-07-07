package beinet.cn.springjpastudy.repository;

/**
 * AaaRepository
 *
 * @author youbl
 * @version 1.0
 * @date 2020/12/30 9:42
 */
public interface Child2Repository extends BaseRepository<Child2> {
    @Override
    default Class getEntityType() {
        return Child2.class;
    }

    @Override
    default String getName() {
        return "child2";
    }
}
