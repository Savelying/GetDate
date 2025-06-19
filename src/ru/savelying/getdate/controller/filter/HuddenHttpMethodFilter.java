package ru.savelying.getdate.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import ru.savelying.getdate.model.Gender;

import java.io.IOException;
import java.util.Locale;

@WebFilter("/*")
public class HuddenHttpMethodFilter implements Filter {
    private static final String METHOD_PARAM = "_method";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        filterConfig.getServletContext().setAttribute("genders", Gender.values());
//        if (config.getServletContext().getAttribute("genders") == null) filterConfig.getServletContext().setAttribute("genders", Gender.values());    }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String paramValue = request.getParameter(METHOD_PARAM);

        if (request.getMethod().equals("POST") && paramValue != null && !paramValue.isBlank()) {
            String method = paramValue.toUpperCase(Locale.ENGLISH);
            HttpServletRequest wrapper = new HttpMethodRequestWrapper(request, method);
            filterChain.doFilter(wrapper, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
        private final String method;

        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method;
        }

        @Override
        public String getMethod() {
            return this.method;
        }
    }
}
