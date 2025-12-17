package kr.java.security.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserAccount extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    private Boolean enabled = true;  // 계정 활성화 여부

    public UserAccount(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = true;
    }

    // (5)-2
    private String provider; // 소셜 로그인을 제공하는 곳
    private String providerId; // OAuth2 제공자에서의 사용자 ID

    // OAuth2 회원가입용 생성자
    public UserAccount(String username, String role, String provider, String providerId) {
        this.username = username;
        this.password = ""; // 소셜로그인은 비밀번호가 없으므로 빈 것 제공.
        this.role = role;
        this.enabled = true;
        this.provider = provider;
        this.providerId = providerId;
    }
}