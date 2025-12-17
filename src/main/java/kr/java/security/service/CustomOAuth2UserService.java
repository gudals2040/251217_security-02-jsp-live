package kr.java.security.service;

import kr.java.security.model.entity.UserAccount;
import kr.java.security.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

// #(5)-4
@Service
@RequiredArgsConstructor
// DefaultOAuth2UserService
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        return super.loadUser(userRequest);
    }

    // #(5)-4-2
    private UserAccount createOAuth2User(String provider, String providerId,String name
    ) {
        String username = provider + "_" + providerId;

        UserAccount newUser = new UserAccount(
            username,
            "ROLE_USER",
            provider,
            providerId
        );

        return userAccountRepository.save(newUser);
    }
}
