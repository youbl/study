package beinet.cn.springjpastudy.mapper;

import beinet.cn.springjpastudy.dto.AaaDto;
import beinet.cn.springjpastudy.repository.Aaa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 自动映射接口。
 * 写完这个接口，需要先进行Maven生成。
 */
@Mapper
public interface AaaConverter {
    AaaConverter INSTANCE = Mappers.getMapper(AaaConverter.class);

    // 注意：MapStruct不支持lombok的Data注解字段，需要在AaaDto上右键 delombok @Data注解
    @Mappings({
            @Mapping(target = "dishId", ignore = true), // 忽略dishId字段不映射
            //@Mapping(target = "restXXXId", source = "restId") // 指定映射字段
    })
    AaaDto entity2dto(Aaa aaa);

    List<AaaDto> entity2dto(List<Aaa> aaa);
}
