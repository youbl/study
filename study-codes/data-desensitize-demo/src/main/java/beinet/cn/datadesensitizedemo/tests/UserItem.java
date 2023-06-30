package beinet.cn.datadesensitizedemo.tests;

import beinet.cn.datadesensitizedemo.services.DesensitizeEnum;
import beinet.cn.datadesensitizedemo.services.NeedDesensitize;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/6/29 20:18
 */
@Data
@Accessors(chain = true)
public class UserItem {
    @NeedDesensitize(DesensitizeEnum.NAME)
    private String username;

    @NeedDesensitize(DesensitizeEnum.PHONE)
    private String phone;

    private String email;

    @NeedDesensitize(DesensitizeEnum.ADDRESS)
    private String addr;

    @NeedDesensitize(DesensitizeEnum.PASSWORD)
    private String accessKey;
}
