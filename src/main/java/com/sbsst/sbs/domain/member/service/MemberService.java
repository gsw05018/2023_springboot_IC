package com.sbsst.sbs.domain.member.service;

import com.sbsst.sbs.domain.member.entity.Member;
import com.sbsst.sbs.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member join(String username, String password, String nickname){
        Member member = Member // Member 클래스의 빌더를 사용하여 객체 생성
                .builder()
                .username(username) // member객체의 username 필드를 주어진 username값으로 설정
                .password(password) // member객체의 password 필드를 주어진 password값으로 설정
                .nickname(nickname) // member객체의 nickname 필드를 주어진 nickname값으로 설정
                .build();

                return memberRepository.save(member);
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }
}

