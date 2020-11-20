package beinet.cn.demospringsecurity.controller;

import beinet.cn.demospringsecurity.security.argument.AuthDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    /**
     * 添加一个注入参数，填充登录信息
     *
     * @param details 注入的信息
     * @return
     */
    @GetMapping("/role")
    @PreAuthorize("isAuthenticated()")
    public String role(AuthDetails details) {
        String ret = "I'm logined ROLE.";
        if (details == null)
            ret += " null";
        else
            ret += " name: " + details.getUserName() + " 角色: " + details.getRole() + " <br>" + details.getUserAgent();
        return ret;
    }

    @GetMapping("/roleRoot")
    @PreAuthorize("hasAnyRole('ROOT')")
    public String roleRoot() {
        return "I'm role root.";
    }
}
