package com.sbsst.sbs.base.rq;

import com.sbsst.sbs.domain.member.entity.Member;
import com.sbsst.sbs.domain.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class Rq {
    // MemberService 객체를 저장하는 private final 필드
    private final MemberService memberService;
    // HttpServletRequest 객체를 저장하는 private final 필드
    private final HttpServletRequest req;
    // HttpServletResponse 객체를 저장하는 private final 필드
    private final HttpServletResponse resp;
    // HttpSession 객체를 저장하는 private final 필드
    private final HttpSession session;
    // Member 객체를 저장하는 private 필드, 초기값은 null
    private Member member = null;
    // User 객체를 저장하는 private final 필드
    private final User user;

    // Rq 객체 생성 시 MemberService, HttpServletRequest, HttpServletResponse, HttpSession 객체를 받아 초기화하는 생성자
    public Rq(MemberService memberService, HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        this.memberService = memberService;
        this.req = req;
        this.resp = resp;
        this.session = session;

        // 현재 로그인한 회원의 인증정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 현재 인증된 사용자가 User 객체인지 확인
        if (authentication.getPrincipal() instanceof User) {
            // User 객체로 형변환하여 user 필드에 저장
            this.user = (User) authentication.getPrincipal();
        } else {
            // 인증된 사용자가 User 객체가 아니면 user 필드를 null로 설정
            this.user = null;
        }
    }

    // 모든 쿠키 값을 문자열 형태로 반환하는 메서드
    public String getAllCookieValuesAsString() {
        StringBuilder sb = new StringBuilder();

        // 현재 요청에서 쿠키 가져오기
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            // 각 쿠키의 이름과 값을 문자열에 추가
            for (Cookie cookie : cookies) {
                sb.append(cookie.getName()).append(": ").append(cookie.getValue()).append("\n");
            }
        }

        return sb.toString();
    }

    // 모든 세션 값을 문자열 형태로 반환하는 메서드
    public String getAllSessionValuesAsString() {
        StringBuilder sb = new StringBuilder();

        // 현재 세션에서 모든 속성 이름 가져오기
        java.util.Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            // 각 세션 속성의 이름과 값을 문자열에 추가
            String attributeName = attributeNames.nextElement();
            sb.append(attributeName).append(": ").append(session.getAttribute(attributeName)).append("\n");
        }

        return sb.toString();
    }

    // 현재 로그인한 회원의 이름을 반환하는 메서드
    private String getLoginedMemberUsername() {
        // 로그아웃 상태라면 null 반환
        if (isLogout()) return null;

        return user.getUsername();
    }

    // 현재 사용자가 로그인 상태인지 여부를 반환하는 메서드
    public boolean isLogin() {
        return user != null;
    }

    // 현재 사용자가 로그아웃 상태인지 여부를 반환하는 메서드
    public boolean isLogout() {
        return !isLogin();
    }

    // 현재 로그인한 회원의 정보를 가져오는 메서드
    public Member getMember() {
        // 로그아웃 상태라면 null 반환
        if (isLogout()) {
            return null;
        }

        if (member == null) {
            // 현재 로그인한 회원의 이름으로 회원 정보 조회
            member = memberService.findByUsername(getLoginedMemberUsername()).get();
        }

        return member;
    }

    // 현재 사용자가 관리자인지 여부를 반환하는 메서드
    public boolean isAdmin() {
        // 로그아웃 상태라면 관리자가 아님
        if (isLogout()) return false;

        // 현재 로그인한 회원이 관리자인지 여부 반환
        return getMember().isAdmin();
    }

    // 세션에 값을 저장하는 메서드
    public void setSession(String name, Object value) {
        session.setAttribute(name, value);
    }

    // 세션에서 값을 가져오는 메서드
    private Object getSession(String name, Object defaultValue) {
        // 세션에서 이름에 해당하는 값 가져오기
        Object value = session.getAttribute(name);

        if (value == null) {
            return defaultValue;
        }

        return value;
    }

    // 세션에서 값을 long 타입으로 가져오는 메서드
    private long getSessionAsLong(String name, long defaultValue) {
        Object value = getSession(name, null);

        if (value == null) return defaultValue;

        return (long) value;
    }

    // 세션에서 값을 제거하는 메서드
    public void removeSession(String name) {
        session.removeAttribute(name);
    }

    // 쿠키를 설정하는 메서드
    public void setCookie(String name, String value) {
        // 쿠키 생성 및 응답에 추가
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    // 쿠키에서 값을 가져오는 메서드
    private String getCookie(String name, String defaultValue) {
        // 현재 요청에서 쿠키 가져오기
        Cookie[] cookies = req.getCookies();

        if (cookies == null) {
            return defaultValue;
        }

        // 이름에 해당하는 쿠키의 값 찾아서 반환
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }

        return defaultValue;
    }

    // 쿠키에서 값을 long 타입으로 가져오는 메서드
    private long getCookieAsLong(String name, int defaultValue) {
        String value = getCookie(name, null);

        if (value == null) {
            return defaultValue;
        }

        return Long.parseLong(value);
    }

    // 쿠키를 제거하는 메서드
    public void removeCookie(String name) {
        // 쿠키 생성 및 응답에 추가하여 제거
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }
}
