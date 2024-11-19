package cn.beinet.deployment.admin.users.mapstruct;

import cn.beinet.deployment.admin.users.dal.entity.Users;
import cn.beinet.deployment.admin.users.dto.UsersDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

/**
 * need add these dependency, and compile with maven:
 * <dependency><groupId>org.mapstruct</groupId><artifactId>mapstruct</artifactId></dependency>
 * <dependency><groupId>org.mapstruct</groupId><artifactId>mapstruct-processor</artifactId></dependency>
 *
 * @author youbl.blog.csdn.net
 * @since 2024-11-19 12:28:00
 */
@Mapper
public interface UsersEntityMapper {
    UsersEntityMapper INSTANCE = Mappers.getMapper(UsersEntityMapper.class);

    @Mapping(source = "lastLoginDate", target = "lastLoginDate", qualifiedByName = "mapTime")
    @Mapping(source = "createTime", target = "createTime", qualifiedByName = "mapTime")
    @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = "mapTime")
    Users dto2entity(UsersDto dto);

    @Mapping(source = "lastLoginDate", target = "lastLoginDate", qualifiedByName = "mapTimestamp")
    @Mapping(target = "lastLoginDateBegin", ignore = true)
    @Mapping(target = "lastLoginDateEnd", ignore = true)
    @Mapping(source = "createTime", target = "createTime", qualifiedByName = "mapTimestamp")
    @Mapping(target = "createTimeBegin", ignore = true)
    @Mapping(target = "createTimeEnd", ignore = true)
    @Mapping(source = "updateTime", target = "updateTime", qualifiedByName = "mapTimestamp")
    UsersDto entity2dto(Users entity);

    List<Users> dto2entity(List<UsersDto> dto);

    List<UsersDto> entity2dto(List<Users> entity);

    @Named("mapTime")
    default LocalDateTime mapTime(Long millisTs) {
        if (millisTs != null) {
            return Timestamp.from(Instant.ofEpochMilli(millisTs)).toLocalDateTime();
        }
        return null;
    }

    // convert to millis timestamp
    @Named("mapTimestamp")
    default Long mapTimestamp(LocalDateTime value) {
        if (value != null) {
            return Timestamp.valueOf(value).getTime();
        }
        return null;
    }
}