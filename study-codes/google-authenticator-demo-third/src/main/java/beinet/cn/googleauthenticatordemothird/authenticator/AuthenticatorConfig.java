package beinet.cn.googleauthenticatordemothird.authenticator;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 新类
 *
 * @author youbl
 * @since 2023/5/30 13:36
 */
@Configuration
@RequiredArgsConstructor
public class AuthenticatorConfig {
    private final CredentialRepository credentialRepository;

    @Bean
    public GoogleAuthenticator getGoogleAuth() {
        GoogleAuthenticator authenticator = new GoogleAuthenticator();
        authenticator.setCredentialRepository(credentialRepository);
        return authenticator;
    }
}
