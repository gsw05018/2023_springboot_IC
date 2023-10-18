package com.sbsst.sbs.domain.member.controller;

import com.sbsst.sbs.base.rq.Rq;
import com.sbsst.sbs.domain.member.entity.Member;
import com.sbsst.sbs.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;


@Controller
@RequestMapping("/usr/member") // 액션 URL의 공통 접두어
@RequiredArgsConstructor
public class MemberController {
    private final Rq rq;

    private final MemberService memberService;

    @GetMapping("/login")
    public String showLogin() {
        if(rq.isLogin()){
            throw new RuntimeException("이미 로그인 되었습니다");
        }

        return "usr/member/login";
    }

    @PostMapping("/login")
    public String login(String username, String password) {
        Optional<Member> opMember = memberService.findByUsername(username);


        if(rq.isLogin()){
            throw new RuntimeException("이미 로그인 되었습니다");
        }

        if ( opMember.isEmpty() ) {
            return "redirect:/usr/member/login?error";
        }

        Member member = opMember.get();

        if ( member.getPassword().equals(password) == false ) {
            return "redirect:/usr/member/login?error";
        }

        rq.setCookie("loginedMemberId", member.getId() + "");
        // 사용자 브라우저에 쿠키를 전송하여 저장
        // name는 쿠키의 이름을 나타내고
        // Value는 쿠키의 값을 나타냄

        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout() {

        if(rq.isLogout()){
            return "redirect:/";
        }

        rq.removeCookie("loginedMemberId");
        // 쿠키의 이름을 기반으로 작동을 하기 때문에 쿠키값을 넣지 않아도 됨
        // 쿠키를 삭제함으로써 로그아웃 기능 구현

        return "redirect:/";
    }

    @GetMapping("/me")
    public String showMe() {

        if(rq.isLogout()){
            return "redirect:/usr/member/login";
        }

        return "usr/member/me";
    }
}