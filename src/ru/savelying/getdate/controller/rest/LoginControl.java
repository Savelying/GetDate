package ru.savelying.getdate.controller.rest;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.service.ProfileService;
import ru.savelying.getdate.validator.LogValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static ru.savelying.getdate.utils.UrlUtils.*;

@Slf4j
@WebServlet(LOGIN_REST_URL)
@MultipartConfig
public class LoginControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LogValidator validator = LogValidator.getInstance();

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        req.getRequestDispatcher(getJspPath(LOGIN_URL)).forward(req, resp);
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (BufferedReader reader = req.getReader()) {
            ProfileDTO profileDTO = objectMapper.readValue(reader, ProfileDTO.class);
            if (validator.validate(profileDTO).isValid()) {
                Optional<ProfileDTO> userDetailsOptional = profileService.login(profileDTO);
                if (userDetailsOptional.isPresent()) {
                    req.getSession().setAttribute("user", userDetailsOptional.get());
                    log.info("Account login successful with email={} and id={}", profileDTO.getEmail(), profileDTO.getId());
                } else resp.sendError(SC_NOT_FOUND);
            } else {
                req.setAttribute("errors", validator.validate(profileDTO).getErrors());
                req.setAttribute("profile", profileDTO);
                resp.sendError(SC_BAD_REQUEST);
            }
        } catch (DatabindException e) {
            log.error(e.getMessage(), e);
            req.setAttribute("errors", e.getMessage());
            resp.sendError(SC_BAD_REQUEST);
        }
    }
}
