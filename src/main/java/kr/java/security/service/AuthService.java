package kr.java.security.service;

import kr.java.security.model.entity.UserAccount;
import kr.java.security.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(String username, String rawPassword) {
        if (userAccountRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        String encodedPassword = passwordEncoder.encode(rawPassword);

        UserAccount user = new UserAccount(
                username,
                encodedPassword,
                "ROLE_USER"
        );

        userAccountRepository.save(user);
    }
}
