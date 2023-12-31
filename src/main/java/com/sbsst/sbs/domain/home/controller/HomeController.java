package com.sbsst.sbs.domain.home.controller;

import com.sbsst.sbs.base.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final Rq rq;

    @GetMapping("/")
    public String showMain() {
        return "usr/home/main";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('admin')")
    public String showAdmin() {

        return "usr/home/admin";
    }


    @GetMapping("/cookies")
    @ResponseBody
    public String showCookies() {
        return rq.getAllCookieValuesAsString();
        // 문자열로 치환한 쿠키코드 화면 반환
    }

    @GetMapping("/sessions")
    @ResponseBody
    public String showSessions() {
        return rq.getAllSessionValuesAsString();
        // 세션코드 문자열로 치환해 화면 반환
    }


}