package beinet.cn.demologmybatis.dal;

import beinet.cn.demologmybatis.dal.entity.Projects;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

public interface ProjectsMapper extends BaseMapper<Projects> {

    @Select("SELECT * FROM projects WHERE name=#{name}")
    Projects getByName(String name);
}
