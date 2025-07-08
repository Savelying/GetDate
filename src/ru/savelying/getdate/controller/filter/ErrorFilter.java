package ru.savelying.getdate.controller.filter;

import com.fasterxml.jackson.databind.DatabindException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.mapper.JsonMapper;
import ru.savelying.getdate.service.bundle.WordBundle;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static jakarta.servlet.RequestDispatcher.ERROR_REQUEST_URI;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static ru.savelying.getdate.utils.UrlUtils.REST_URL;

@Slf4j
@WebFilter(value = "/*", dispatcherTypes = DispatcherType.ERROR)
public class ErrorFilter implements Filter {
    private final JsonMapper jsonMapper = JsonMapper.getInstance();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HashMap<String, String> errorMap = new HashMap<>();
        Object errors = request.getAttribute("errors");

        if (errors instanceof List<?>) {
            WordBundle wordBundle = (WordBundle) request.getAttribute("wordBundle");
            for (int i = 0; i < ((List<?>) errors).size(); i++) {
                errorMap.put("message" + i, wordBundle.getWord(((List<?>) errors).get(i).toString()));
            }
        }

        if (response.getStatus() >= SC_INTERNAL_SERVER_ERROR) {
            UUID errorUuid = UUID.randomUUID();
            request.setAttribute("errorUuid", errorUuid);
            Throwable e = (Throwable) request.getAttribute(ERROR_EXCEPTION);
            log.error("Unexpected error {}:", errorUuid, e);
        } else {
            log.warn("Code: {}; Errors: {}", response.getStatus(), errorMap);
        }

        if (((String) request.getAttribute(ERROR_REQUEST_URI)).startsWith(REST_URL)) {
            if (!errorMap.isEmpty()) {
                try (PrintWriter writer = response.getWriter()) {
                        jsonMapper.writeValue(writer, errorMap);
                } catch (DatabindException ex) {
                    throw new IOException(ex);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
