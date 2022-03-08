package beinet.cn.mapstructdemo.entityMapper;

import beinet.cn.mapstructdemo.dto.AaaDto;
import beinet.cn.mapstructdemo.dto.BbbDto;
import beinet.cn.mapstructdemo.entity.Aaa;
import beinet.cn.mapstructdemo.entity.Bbb;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Description:
 * 注意，修改此类，要点右边的 maven compile，进行编译
 *
 * @author : youbl
 * @create: 2022/3/8 20:23
 */
@Mapper
public interface EntityMapper {
    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    Aaa dto2entity(AaaDto dto);

    List<Aaa> dto2entity(List<AaaDto> dto);

    AaaDto entity2dto(Aaa entity);

    List<AaaDto> entity2dto(List<Aaa> entity);


    @Mapping(source = "desc2", target = "description2") // 字段名不同，要要注解指定
    @Mapping(target = "id2", ignore = true)        // 不映射目标字段
    Bbb dto2entity(BbbDto dto);

    // 因为类型擦除，所以 dto2entity2 跟上面的 dto2entity 具体相同的参数定义，因此要改函数名
    List<Bbb> dto2entity2(List<BbbDto> dto);

    BbbDto entity2dto(Bbb entity);

    List<BbbDto> entity2dto2(List<Bbb> entity);
}
