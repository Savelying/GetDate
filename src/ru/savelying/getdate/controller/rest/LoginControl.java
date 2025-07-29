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
import ru.savelying.getdate.mapper.JsonMapper;
import ru.savelying.getdate.service.ProfileService;
import ru.savelying.getdate.validator.LogValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static ru.savelying.getdate.utils.UrlUtils.*;

@Slf4j
@WebServlet(REST_URL + LOGIN_URL)
@MultipartConfig
public class LoginControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final JsonMapper jsonMapper = JsonMapper.getInstance();
    private final LogValidator validator = LogValidator.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try (BufferedReader reader = req.getReader()) {
            ProfileDTO profileDTO = jsonMapper.readValue(reader, ProfileDTO.class);
            if (validator.validate(profileDTO).isValid()) {
                Optional<ProfileDTO> user = profileService.login(profileDTO);
                if (user.isPresent()) req.getSession().setAttribute("user", user.get());
                else {
                    req.setAttribute("error", validator.validate(profileDTO).getErrors());
                    resp.sendError(SC_NOT_FOUND);
                }
            } else {
                req.setAttribute("errors", validator.validate(profileDTO).getErrors());
                req.setAttribute("profile", profileDTO);
                resp.sendError(SC_BAD_REQUEST);
            }
        } catch (DatabindException e) {
            req.setAttribute("errors", List.of(e.getLocalizedMessage(), e.getOriginalMessage()));
            resp.sendError(SC_BAD_REQUEST);
        }
    }
}
