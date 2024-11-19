package cn.beinet.deployment.admin.users.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.beinet.deployment.admin.users.dal.UsersMapper;
import cn.beinet.deployment.admin.users.dal.entity.Users;
import cn.beinet.deployment.admin.users.dto.UsersDto;
import cn.beinet.deployment.admin.users.mapstruct.UsersEntityMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 *
 * @author youbl.blog.csdn.net
 * @since 2024-11-19 12:28:00
 */
@Service
public class UsersService extends ServiceImpl<UsersMapper, Users> {
    private final UsersEntityMapper entityMapper = UsersEntityMapper.INSTANCE;

    public List<UsersDto> search(UsersDto dto) {
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        if (dto != null) {
            wrapper.eq(dto.getId() != null, Users::getId, dto.getId());
            wrapper.eq(StringUtils.hasLength(dto.getName()), Users::getName, dto.getName());
            wrapper.eq(StringUtils.hasLength(dto.getUserPassword()), Users::getUserPassword, dto.getUserPassword());
            wrapper.eq(StringUtils.hasLength(dto.getUserEmail()), Users::getUserEmail, dto.getUserEmail());
            wrapper.eq(dto.getLastLoginDate() != null, Users::getLastLoginDate, dto.getLastLoginDate());
            wrapper.ge(dto.getLastLoginDateBegin() != null, Users::getLastLoginDate, dto.getLastLoginDateBegin());
            wrapper.le(dto.getLastLoginDateEnd() != null, Users::getLastLoginDate, dto.getLastLoginDateEnd());
            wrapper.eq(dto.getStatus() != null, Users::getStatus, dto.getStatus());
            wrapper.eq(dto.getIsBoss() != null, Users::getIsBoss, dto.getIsBoss());
            wrapper.eq(dto.getIsAdmin() != null, Users::getIsAdmin, dto.getIsAdmin());
            wrapper.eq(dto.getTenantId() != null, Users::getTenantId, dto.getTenantId());
            wrapper.eq(dto.getCreateTime() != null, Users::getCreateTime, dto.getCreateTime());
            wrapper.ge(dto.getCreateTimeBegin() != null, Users::getCreateTime, dto.getCreateTimeBegin());
            wrapper.le(dto.getCreateTimeEnd() != null, Users::getCreateTime, dto.getCreateTimeEnd());
            wrapper.eq(dto.getUpdateTime() != null, Users::getUpdateTime, dto.getUpdateTime());
            wrapper.eq(dto.getDelflag() != null, Users::getDelflag, dto.getDelflag());
            wrapper.eq(dto.getInviteUserId() != null, Users::getInviteUserId, dto.getInviteUserId());
            wrapper.eq(StringUtils.hasLength(dto.getInviteToken()), Users::getInviteToken, dto.getInviteToken());
            wrapper.eq(StringUtils.hasLength(dto.getMfaKey()), Users::getMfaKey, dto.getMfaKey());
            wrapper.eq(StringUtils.hasLength(dto.getPicture()), Users::getPicture, dto.getPicture());
            wrapper.eq(dto.getIsTest() != null, Users::getIsTest, dto.getIsTest());
            wrapper.eq(StringUtils.hasLength(dto.getMemo()), Users::getMemo, dto.getMemo());
            wrapper.eq(StringUtils.hasLength(dto.getUserIp()), Users::getUserIp, dto.getUserIp());
            wrapper.eq(StringUtils.hasLength(dto.getUserLoc()), Users::getUserLoc, dto.getUserLoc());

        }
        wrapper.orderByDesc(Users::getId).last(" LIMIT 123");
        return entityMapper.entity2dto(list(wrapper));
    }

    // find by primary key
    public UsersDto findById(Long id) {
        return entityMapper.entity2dto(getById(id));
    }

    // insert or update data
    public Long saveUsers(UsersDto dto) {
        Users entity = entityMapper.dto2entity(dto);
        boolean result = (entity.getId() != null) ?
                updateById(entity) : save(entity);
        if (!result) {
            throw new RuntimeException("saveError: " + dto);
        }
        return entity.getId();
    }
}