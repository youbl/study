package benet.cn.springfeignstudy;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Service;

/**
 * FeignRequestInterceptor
 *
 * @author youbl
 * @version 1.0
 * @date 2021/1/7 17:44
 */
@Service
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header("X-Context", "acc=3189,mer=193818,agent.cus=0,agent.name=体验店,agent.id=1005617,agent.rea=自动化测试姓名,agent.acc=3189,agent.mob=18750101896,emp.name=体验店,emp.id=1005617,env.tt=Shandian,env.orip=120.35.40.146,env.rapp=35,env.pt=Shandian,env.trace=BN49-9QQvz-s,env.ei=607fd4b3-3bee-4e32-9d13-eb8f4bcafa52");
    }
}
