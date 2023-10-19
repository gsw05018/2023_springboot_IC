package com.sbsst.sbs.base.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import com.sbsst.sbs.standard.util.Ut;

import java.io.IOException;


public class CustomSimpleUrlAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        setDefaultFailureUrl("/usr/member/login?errorMsg=" + Ut.url.encode("올바르지 않은 회원정보 입니다."));
        super.onAuthenticationFailure(request, response, exception);
    }
}