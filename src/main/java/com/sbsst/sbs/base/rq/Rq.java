package com.sbsst.sbs.base.rq;

import com.sbsst.sbs.domain.member.entity.Member;
import com.sbsst.sbs.domain.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component // 컴포넌트 스캔하여 빈으로 등록하도록 지시
@RequestScope // 각 HTTP 요청에 대해 새로운 빈 인스턴스가 생성되도록 지정
public class Rq {
    private final MemberService memberService; // 인스턴스의 의존성 주압을 통해 가져옴
    private final HttpServletRequest req; // 현재 HTTP 요청에 대한 정보를 담고 있는 객첵
    private final HttpServletResponse resp; // 응답에 대한 정보를 담고 있는 객체

    private  final HttpSession session; //  현재 세션에 대한 정보를 담고 있는 객체

    private Member member = null;

    public Rq(MemberService memberService, HttpServletRequest req, HttpServletResponse resp, HttpSession session){
        // 의존성 주입을 통해 필요한 객체를 초기화
        this.memberService = memberService;
        this.req = req;
        this.resp = resp;
        this.session = session;
    }

    public String getAllCookieValueAsString(){ // 현재 요청의 모든 쿠키값을 문자열로 반환
        StringBuilder sb = new StringBuilder();

        Cookie[] cookies = req.getCookies(); // 현재 요청으로부터 모든 쿠키를 가져옴
        if(cookies != null){
            for(Cookie cookie : cookies){
                sb.append(cookie.getName()).append(": ").append(cookie.getValue()).append("\n");
                // 각 쿠키의 이름과 값을 문자열에 추가
            }
        }
        return sb.toString(); // 모든 쿠키정보를 포함한 문자열 반환
    }

    public String getAllSessionValuesAsString() {
        StringBuilder sb = new StringBuilder();

        java.util.Enumeration<String> attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            sb.append(attributeName).append(": ").append(session.getAttribute(attributeName)).append("\n");
        }

        return sb.toString();
    }



    private long getLoginedMemberId() {
        return getSessionAsLong("loginedMemberId", 0);
        // loginedMemberId 세견값 정수로 가져옴
        // getCookieAsInt 메서드를 사용하여 loginedMemberId 세션 값을 가져오고 정수로 변환하여 반환
        // 존재하지 않을 시 기본값 반환
    }

    public boolean isLogin() {
        return getLoginedMemberId() != 0;
        // 현재 사용자가 로그인한 상태지인 확인
        // 사용자가 0이 아닐 시 true반환
    }

    public boolean isLogout() {
        return !isLogin();
        // 사용자가 로그인 아닐시 true반환
    }

    public boolean isAdmin() {
        if(isLogout()){
            return false;
        }
        return getMember().isAdmin();
        // 사용자가 관리자일시 true반환
    }

    public Member getMember() {
        if ( isLogout() ) {
            return null;
        }

        if (member == null) {
            long loginedMemberId = getLoginedMemberId();

            if (loginedMemberId != 0) {
                member = memberService.findById(loginedMemberId).get();
            }
            // 현재 로그인딘 멤버의 정보를 가져옴
            // 로그아웃 상태인지 확인
            // null인경우 해당 멤버의 ID를 가져와 ID에 대한 멤버를 가져옴
        }

        return member;
    }

    // session function
    public void setSession(String name, Object value) {
        session.setAttribute(name, value);
    }

    public void removeSession(String name) {
        session.removeAttribute(name);
    }

    private Object getSession(String name, Object defaultValue){
        Object value = session.getAttribute(name);

        if(value== null){
            return defaultValue;
        }
        return value;
    }

    private long getSessionAsLong(String name, long defaultValue){
        Object value = getSession(name, null);

        if(value == null){
            return defaultValue;


        }
        return (long) value;

    }

    // cookie function
    public void setCookie(String name, String value) {

        Cookie cookie = new Cookie(name,value); // 이름과 값을 매개변수로 받아와 객체 생성
        cookie.setPath("/"); // 경로 설정
        resp.addCookie(cookie); // 생성된 쿠키는 resp.Cookie 메소드를 통해 응답에 추가

    }

    public void removeCookie(String name) {

        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0); // 쿠키의 유효시간을 0으로 설정하여 무효화
        cookie.setPath("/"); // 쿠키의 경로 지정
        resp.addCookie(cookie);

    }

    private String getCookie(String name, String defaultValue) {
        Cookie[] cookies = req.getCookies();

        if (cookies == null) {
            return defaultValue;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }

        return defaultValue;
        // 주어진 쿠키이름에 해당하는 값을 가져오는 역할, 요청된 쿠키 배열을 검사하여 해당 이름과 일치하는 쿠키를 찾고 일치하는 쿠키값을 반환 없을시 기본값을 밚환
    }

    private long getCookieAsLong(String name, long defaultValue) {
        String value = getCookie(name, null);

        if (value == null) {
            return defaultValue;
        }

        return Long.parseLong(value);
        // 문자열 형식의 쿠키 값을 정수로 변환하여 반환
        // getCookie 메서드를 사용하여 쿠키 값을 가져옴
        // 가져온 문자열값을 치환
    }

}

