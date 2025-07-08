package ru.savelying.getdate.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.savelying.getdate.service.bundle.WordBundleEn;
import ru.savelying.getdate.service.bundle.WordBundleRu;
import ru.savelying.getdate.service.bundle.WordBundle;

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

        WordBundle wordBundle = lang.equals("ru") ? WordBundleRu.getInstance() : WordBundleEn.getInstance();
        request.setAttribute("wordBundle", wordBundle);

        filterChain.doFilter(request, response);
    }
}
