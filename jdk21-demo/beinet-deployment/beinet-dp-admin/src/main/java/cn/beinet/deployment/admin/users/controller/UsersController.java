package cn.beinet.deployment.admin.users.controller;

import cn.beinet.core.base.commonDto.ResponseData;
import cn.beinet.deployment.admin.users.dto.UsersDto;
import cn.beinet.deployment.admin.users.service.UsersService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 * @author youbl.blog.csdn.net
 * @since 2024-11-19 12:28:00
 */
@RestController
@RequiredArgsConstructor
public class UsersController {
    private final UsersService service;

    @PostMapping("/users/all")
    public ResponseData<List<UsersDto>> findAll(@RequestBody UsersDto dto) {
        return ResponseData.ok(service.search(dto));
    }

    @GetMapping("/users")
    public ResponseData<UsersDto> findById(@NonNull Long id) {
        return ResponseData.ok(service.findById(id));
    }


    @DeleteMapping("/users")
    // @EventLog(subType = SubType.Users_DEL)
    public ResponseData<Boolean> delById(@NonNull Long id) {
        return ResponseData.ok(service.removeById(id));
    }

    @PutMapping("/users")
    // @EventLog(subType = SubType.Users_UPDATE)
    public ResponseData<Long> updateById(@RequestBody @NonNull UsersDto dto) {
        Assert.notNull(dto.getId(), "update must set primary key.");
        Long newId = service.saveUsers(dto);
        return ResponseData.ok(newId);
    }

    @PostMapping("/users")
    // 要上报新增的主键，因此这里不能用注解@EventLog
    public ResponseData<Long> insert(@RequestBody @NonNull UsersDto dto) {
        // clear primary key before insert
        dto.setId(null);

        Long newId = service.saveUsers(dto);
        dto.setId(newId);
        //LogUtils.reportLog(SubType.Users_ADD, dto); // 事件上报代码
        return ResponseData.ok(newId);
    }

}