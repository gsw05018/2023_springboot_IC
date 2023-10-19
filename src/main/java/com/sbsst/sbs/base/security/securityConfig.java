package com.sbsst.sbs.base.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 해당 클래스가 하나 이상의 Bean 메서드를 제공하고 스프링 컨테이너가 이 클래스를 사용하여 빈 정의 및 구성을 한다는 것을 나타냄
@EnableWebSecurity // 스프링 애플리케이션에 웹 보안 구성을 활성화 한다는 것을 나타냄
@EnableMethodSecurity // 메서드에 엑세스를 제한하고 사용자 권한을 확인하는 기능 활성화
@RequiredArgsConstructor // 초기화되지 않은 final 필드나 @NonNull이 붙은 필드에 대한 생성자 생성
public class securityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws  Exception{
        http
                .formLogin(
                        formLogin -> formLogin
                                .loginPage("/usr/member/login")
                                .failureHandler(new CustomSimpleUrlAuthenticationFailureHandler())
                )
                .logout(
                        logout -> logout
                                .logoutUrl("/usr/member/logout")
                );
        return http.build();
        // HttpSecurity를 매개변수로 받아 SecurityFilterChain을 반환하는 filterChain빈을 생성
        // HttpSecurity는 스프링 시큐리티 구성의 핵심이며, 애플리에키이션에 대한 보안 구성을 가능하게 함
        // http.build()는 HttpSecurity의 구성을 빌드하고 반환
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
        // 비밀번호를 안전하게 저장하고 인증할 수 있도록 하는 PasswordEncoder빈 생성
        // BCrypt알고리즘을 사용하는 BCryptPasswordEncoder반환
        // 사용자의 비밀번호를 해시하여 저장
        // 인증과정에서 입력된 비밀번호를 저장된 해시값과 비교하여 검증
    }
}
