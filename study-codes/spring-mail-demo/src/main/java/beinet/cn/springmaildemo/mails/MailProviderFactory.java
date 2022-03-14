package beinet.cn.springmaildemo.mails;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 *
 * @author : youbl
 * @create: 2022/3/14 15:11
 */
@Component
@RequiredArgsConstructor
public class MailProviderFactory {
    private final List<MailProvider> allMailProviders;

    public MailProvider getProvider(String mailAddress) {
        for (MailProvider provider : allMailProviders) {
            if (provider.match(mailAddress)) {
                return provider;
            }
        }
        return null;
    }
}
