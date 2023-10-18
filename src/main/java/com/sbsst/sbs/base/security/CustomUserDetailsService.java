package com.sbsst.sbs.base.security;

import com.sbsst.sbs.domain.member.entity.Member;
import com.sbsst.sbs.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    // MemberRepository 객체를 저장하는 private final 필드
    private final MemberRepository memberRepository;

    // UserDetailsService 인터페이스의 loadUserByUsername 메서드 재정의
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 주어진 username에 해당하는 Member를 찾거나, 찾지 못하면 예외를 던짐
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username(%s) not found".formatted(username)));
        // Member를 바탕으로 User 객체를 생성하여 반환
        return new User(member.getUsername(), member.getPassword(), member.getGrantedAuthorities());
    }
}
