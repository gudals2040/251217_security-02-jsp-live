package kr.java.security.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // #(1)-1
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                    // jakarta.servlet.DispatcherType
                    .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                    .requestMatchers("/", "/auth/**", "/css/**", "/js/**").permitAll()
                    .requestMatchers("/memo/**").authenticated()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )

            .formLogin(form -> form
                    .loginPage("/auth/login")
                    .loginProcessingUrl("/auth/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/", true)
//                    .successHandler()
                    // -> request.getSession().setAttribute("REDIRECT_URL", requestedUrl);
                    .failureUrl("/auth/login?error=true")
                    // #(4)-2-2
                    .failureHandler(authenticationFailureHandler())
                    .permitAll()
            )

            .logout(logout -> logout
                    .logoutUrl("/auth/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
            )

            // #(1)-2
            .exceptionHandling(ex -> ex
//                    // 인증 <- 로그인이 안된 사람
//                    .authenticationEntryPoint(((request, response, authException) -> response.sendRedirect("/auth/login")))
//                    // 인가 <- 인증은 되었는데 권한이 없는 사람 => AccessDeniedException
//                    .accessDeniedPage("/error/403") // Forward (POST) / 경로 -> jsp 파일
                    // #(4)-1-3
                    .authenticationEntryPoint(authenticationEntryPoint())
                    .accessDeniedHandler(accessDeniedHandler())
            )
        ;


        return http.build();
    }

    // #(4)-1-1 : 로그인 후 접속 시도한 페이지로 연결
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return ((request, response, authException) -> {
           // 로그인이 안되어 있을 때 -> 로그인이 되면 로그인을 발생시킨 페이지로 이동
           String requestedUrl = request.getRequestURI();
           if (request.getQueryString() != null) {
               requestedUrl += "?" + request.getQueryString();
           }
           request.getSession().setAttribute("REDIRECT_URL", requestedUrl);

           response.sendRedirect("/auth/login?redirect=true");
        });
    }

    // #(4)-1-2
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return ((request, response, accessDeniedException) -> {
           // 403 에러페이지로 포워드
           request.setAttribute("errorMessage", "접근권한이 없습니다.");
           request.getRequestDispatcher("/error/403").forward(request, response);  // POST
        });
    }

    // #(4)-2-1
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            String errorMessage;

            if (exception instanceof BadCredentialsException) {
                errorMessage = "아이디 또는 비밀번호가 올바르지 않습니다.";
            } else if (exception instanceof DisabledException) {
                errorMessage = "비활성화된 계정입니다. 관리자에게 문의하세요.";
            } else if (exception instanceof LockedException) {
                errorMessage = "잠긴 계정입니다. 관리자에게 문의하세요.";
            } else {
                errorMessage = "로그인에 실패했습니다.";
            }

            // URL 인코딩하여 파라미터로 전달
            String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
            response.sendRedirect("/auth/login?error=true&message=" + encodedMessage);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
