package com.sbsst.sbs.domain.member.controller;

import com.sbsst.sbs.domain.member.entity.Member;
import com.sbsst.sbs.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/usr/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String showLogin(){
        return "usr/member/login";
    }

    @PostMapping("login")
    public String login(String username, String password){
        Optional<Member> opMember = memberService.findByUsername(username);
        // 주어진 username에 해당하는 member를 가져옴
        
        if(opMember.isEmpty()){
            return "redirect:/usr/member/login?error";
        } // 존재하지 않는 경우 해당경로 이동

        Member member = opMember.get();
        // Optional 객체에서 Member 객체 추출

        if( member.getPassword().equals(password) == false){
            return "redirect:/usr/member/login?error";
        } // 입력한 비밀번호가 오류 시 오류페이지 이동
        return "redirect:/"; // 성공한경우 메인페이지 이동
    }

    @GetMapping("/me")
    public String showMe(){
        return "usr/member/me";
    }
}
