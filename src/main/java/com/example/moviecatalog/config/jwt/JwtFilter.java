package com.example.moviecatalog.config.jwt;

import com.example.moviecatalog.config.CustomUserDetails;
import com.example.moviecatalog.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

@Component
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public final String AUTHORIZATION = "Authorization";
    private JwtProvider jwtProvider;
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String token;
        CustomUserDetails customUserDetails;
        String userLogin;
        UsernamePasswordAuthenticationToken auth;

        token = getTokenFromRequest((HttpServletRequest) servletRequest);

        if (token != null && jwtProvider.validateToken(token)) {
            userLogin = jwtProvider.getLoginFromToken(token);
            customUserDetails = customUserDetailsService.loadUserByUsername(userLogin);
            auth = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        String result = null;

        if (hasText(bearer) && bearer.startsWith("Bearer ")) {
            result = bearer.substring(7);
        }

        return result;
    }
}
