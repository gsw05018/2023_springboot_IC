package com.sbsst.sbs.base.rq;

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
                sb.append(cookie.getName()).append(":").append(cookie.getValue()).append("\n");
                // 각 쿠키의 이름과 값을 문자열에 추가
            }
        }
        return sb.toString(); // 모든 쿠키정보를 포함한 문자열 반환
    }

    public String getAllSessionValueAsString(){ // 현재 세션의 모든 값들을 문자열로 반환
        StringBuilder sb = new StringBuilder();

        java.util.Enumeration<String>  attributeNames = session.getAttributeNames();
        // 현재 세션에 있는 모든 속성 이름을 열거형으로 가져옴

        while (attributeNames.hasMoreElements()){
            String attibuteName = attributeNames.nextElement();
            sb.append(attibuteName).append(": ").append(session.getAttribute(attibuteName)).append("\n");
            // 각 세션 속성의 이름과 값을 문자열에 추가
        }
        return sb.toString(); // 모든 세션정보를 포함한 문자열 반환
    }

    public void setCookie(String name, String Value) {

        Cookie cookie = new Cookie(name, ""); // 이름과 값을 매개변수로 받아와 객체 생성
        cookie.setPath("/"); // 경로 설정
        resp.addCookie(cookie); // 생성된 쿠키는 resp.Cookie 메소드를 통해 응답에 추가

    }

    public void removeCookie(String name) {

        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0); // 쿠키의 유효시간을 0으로 설정하여 무효화
        cookie.setPath("/"); // 쿠키의 경로 지정
        resp.addCookie(cookie);



    }
}
