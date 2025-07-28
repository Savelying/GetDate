package ru.savelying.getdate.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.model.Role;

import java.io.IOException;
import java.util.List;

import static ru.savelying.getdate.utils.UrlUtils.*;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ProfileDTO user = (ProfileDTO) request.getSession().getAttribute("user");

        if (!request.getRequestURI().equals(LOGIN_REST_URL) && PRIVATE_PATHS.stream().anyMatch(request.getRequestURI()::startsWith)) {
            if (user != null) {
                if (request.getRequestURI().startsWith(GETDATE_URL) || user.getRole() == Role.ADMIN || (!request.getRequestURI().startsWith(REST_URL) && user.getId().toString().equals(request.getParameter("id")))) {
                    filterChain.doFilter(request, response);
                } else {
                    String message = String.format("User with id %s and role %s try to use %s endpoint with query parameter %s",
                                    user.getId(), user.getRole(), request.getRequestURI(), request.getParameter("id"));
                    request.setAttribute("errors", List.of(message));
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            } else response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (user != null && ENTRY_PATHS.contains(request.getRequestURI())) response.sendRedirect(PROFILE_URL + "?id=" + user.getId());
        else filterChain.doFilter(request, response);
    }
}
