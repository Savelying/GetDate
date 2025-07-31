package ru.savelying.getdate.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import ru.savelying.getdate.model.Gender;
import ru.savelying.getdate.model.Role;
import ru.savelying.getdate.model.Status;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static jakarta.servlet.DispatcherType.FORWARD;
import static jakarta.servlet.DispatcherType.REQUEST;
import static ru.savelying.getdate.utils.StringUtils.isBlank;

@WebFilter(value = "/*", dispatcherTypes = {REQUEST, FORWARD})
public class HuddenHttpMethodFilter implements Filter {
    private static final String METHOD_PARAM = "_method";

//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        filterConfig.getServletContext().setAttribute("genders", Gender.values());
//        filterConfig.getServletContext().setAttribute("statuses", Status.values());
//        filterConfig.getServletContext().setAttribute("roles", Role.values());
//        filterConfig.getServletContext().setAttribute("availPageSizes", List.of(10, 20, 50, 100));
//    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getDispatcherType() == FORWARD && request instanceof HttpMethodRequestWrapper) {
            ((HttpMethodRequestWrapper) request).setMethod("GET");
        } else {
            String paramValue = request.getParameter(METHOD_PARAM);

            if (request.getMethod().equals("POST") && !isBlank(paramValue)) {
                String method = paramValue.toUpperCase(Locale.ENGLISH);
                request = new HttpMethodRequestWrapper(request, method);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Setter
    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
        private String method;

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
