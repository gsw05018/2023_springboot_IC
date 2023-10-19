package com.sbsst.sbs.domain.member.controller;

import com.sbsst.sbs.base.rq.Rq;
import com.sbsst.sbs.domain.member.entity.Member;
import com.sbsst.sbs.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("isAnonymous()")
    public String showLogin() {

        return "usr/member/login";
    }

    @GetMapping("/me")
    public String showMe() {

        if(rq.isLogout()){
            return "redirect:/usr/member/login";
        }

        return "usr/member/me";
    }

    @GetMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String showModify(){
        return "usr/member/modify";
    }

    @PostMapping("/modify")
    @PreAuthorize("isAuthenticated()")
    public String modify(String password, String nickname){
        Member member = rq.getMember();
        memberService.modify(member, password, nickname);

        return "redirect:/usr/member/me";
    }
}