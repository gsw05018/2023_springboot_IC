package com.sbsst.sbs.base.initData.base.rq;

import com.sbsst.sbs.domain.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class Rq {
    private final MemberService memberService;
    private final HttpServletRequest req;
    private final HttpServletResponse resp;

    private  final HttpSession session;

    public Rq(MemberService memberService, HttpServletRequest req, HttpServletResponse resp, HttpSession session){
        this.memberService = memberService;
        this.req = req;
        this.resp = resp;
        this.session = session;
    }

    public String getAllCookieValueAsString(){
        StringBuilder sb = new StringBuilder();

        Cookie[] cookies = req.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                sb.append(cookie.getName()).append(":").append(cookie.getValue()).append("\n");
            }
        }
        return sb.toString();
    }

    public String getAllSessionValueAsString(){
        StringBuilder sb = new StringBuilder();

        java.util.Enumeration<String>  attributeNames = session.getAttributeNames();
        while (attributeNames.hasMoreElements()){
            String attibuteName = attributeNames.nextElement();
            sb.append(attibuteName).append(": ").append(session.getAttribute(attibuteName)).append("\n");
        }
        return sb.toString();
    }

}
