package kr.java.security.service;

import kr.java.security.model.entity.UserAccount;
import kr.java.security.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;

// #(5)-4
@Service
@RequiredArgsConstructor
@Slf4j // log
// DefaultOAuth2UserService
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserAccountRepository userAccountRepository;

    // #(5)-4-3
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // 상속 받은 상위 클래스의 메서드
        String provider = userRequest.getClientRegistration().getRegistrationId(); // github, google, kakao...

        String providerId; // provider -> ID.
        String name;

        switch (provider) {
            case "github":
                Object idAttribute = oAuth2User.getAttribute("id");
                providerId = String.valueOf(idAttribute);
                name = oAuth2User.getAttribute("login"); // 깃허브 사용자명
                log.info("providerId: {}, name: {}", providerId, name);
                break;
            case "google":
            case "kakao":
            default:
                throw new OAuth2AuthenticationException("지원하지 않는 로그인 제공자");
        }

        // 기존 사용자 조회 또는 새 사용자 생성
        UserAccount user = userAccountRepository
                .findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> createOAuth2User(provider, providerId, name));

        // 일반적 구조 -> DB에 username provider 구별 -> 추가적 작업
        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority(user.getRole())),
                oAuth2User.getAttributes(),
                "id"
        );
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
