package com.sbsst.sbs.domain.member.service;

import com.sbsst.sbs.domain.member.entity.Member;
import com.sbsst.sbs.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service // 서비스 제공자로 표시하는데 사용
@Transactional(readOnly = true) // 클래스 수준에서 사용되며, 클래스의 모든 메서드가 트랜잭션 내에서 실행되어야 함을 지정
// readOnly속성은 트랜잭션이 읽기 전용임을 나타내며 수정 시도는 예외를 유발
@RequiredArgsConstructor // 필수 인자를 갖는 생성자 생성
public class MemberService {
    private final MemberRepository memberRepository; // memberRepository 인터페이스 주입
    private final PasswordEncoder passwordEncoder; // passwordEncoder 인터페이스 주입

    @Transactional // 메서드 수준에서 사용되며, 메서드가 트랜잭션 내에서 실행되어야 함을 지정, 런타임 예외가 발생시 트랜잭션이 롤백이 됨
    public Member join(String username, String password, String nickname){
        Member member = Member // Member 클래스의 빌더를 사용하여 객체 생성
                .builder()
                .username(username) // member객체의 username 필드를 주어진 username값으로 설정
                .password(passwordEncoder.encode(password)) // member객체의 password 필드를 주어진 password값으로 설정
                .nickname(nickname) // member객체의 nickname 필드를 주어진 nickname값으로 설정
                .build();

                return memberRepository.save(member); // 회원정보 저장
    }

    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }
    // username으로 회원 정보 조회

    public Optional<Member> findById(long id) {
        return memberRepository.findById(id);
    }
    // id로 회원조회

    @Transactional // 메서드 수준에서 사용되며, 메서드가 트랜잭션 내에서 실행되어야 함을 지정, 런타임 예외가 발생시 트랜잭션이 롤백이 됨
    public void modify(Member member, String password, String nickname) {
        if(password != null && password.length() > 0){
            member.setPassword(passwordEncoder.encode(password));
            // 새로운 비밀번호가 주어진 경우, 비밀번호를 암호화하여 설정
        }
        if(nickname != null && nickname.length() > 0){
            member.setNickname(nickname);
            // 새로운 닉네임이 주어진경우 닉네임설정
        }
    }
}

