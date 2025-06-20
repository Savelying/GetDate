package ru.savelying.getdate.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.savelying.getdate.service.WordBundle;

import java.io.IOException;
import java.util.Arrays;

@WebFilter("/*")
public class LanguageFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        Cookie[] cookies = request.getCookies() != null ? request.getCookies() : new Cookie[]{};
        String lang = Arrays.stream(cookies)
                .filter(cookie -> "lang".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse("en");

        WordBundle wordBundle = new WordBundle(lang);
        request.setAttribute("wordBundle", wordBundle);

        filterChain.doFilter(request, response);
    }
}
