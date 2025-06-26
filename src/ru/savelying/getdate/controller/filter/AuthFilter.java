package ru.savelying.getdate.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.mapper.ProfileMapper;
import ru.savelying.getdate.model.Role;

import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter implements Filter {
    private final static ProfileMapper profileMapper = ProfileMapper.getInstance();
    private final static Set<String> PRIVATE_PATHS = Set.of("/profile", "/email");

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

       if (PRIVATE_PATHS.contains(request.getRequestURI())) {
           ProfileDTO user = (ProfileDTO) request.getSession().getAttribute("user");
           if (user != null) {
               if (user.getRole() == Role.ADMIN || user.getId().toString().equals(request.getParameter("id"))) {
                   filterChain.doFilter(request, response);
               } else response.sendError(HttpServletResponse.SC_FORBIDDEN);
           } else response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

       } else filterChain.doFilter(request, response);
    }
}
