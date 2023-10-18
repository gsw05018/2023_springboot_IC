package com.sbsst.sbs.domain.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity // jpa엔티티
@EqualsAndHashCode // Equals, HashCode 자동생성
@NoArgsConstructor // 매개변수가 없는 생성자 생성
@AllArgsConstructor // 매개변수가 있는 생성자 자동생성
@Builder // 빌더 패턴을 사용하여 객체를 생성할 수 있는 빌더 클래스생성
@Getter
public class Member {
    @EqualsAndHashCode.Include // 해당 필드만을 기반으로 equals, hashCode 메소드 생성
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String nickname;

    public boolean isAdmin(){
        return "admin".equals(username);
    }
    // 현재 객체의 username 필드를 검사여 admin과 일치하는 지 여부 확인
    // 관리자와 일반 사용자를 구분하여 특정 작업수행및 권한부여
}
