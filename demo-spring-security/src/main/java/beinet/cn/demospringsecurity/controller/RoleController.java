package beinet.cn.demospringsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {
    @GetMapping("/role")
    @PreAuthorize("isAuthenticated()")
    public String role() {
        return "I'm logined.";
    }

    @GetMapping("/roleRoot")
    @PreAuthorize("hasAnyRole('ROOT')")
    public String roleRoot() {
        return "I'm role root.";
    }
}
