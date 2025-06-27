package ru.savelying.getdate.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.savelying.getdate.dto.ProfileDTO;
import ru.savelying.getdate.mapper.ProfileMapper;
import ru.savelying.getdate.service.ProfileService;
import ru.savelying.getdate.validator.LogValidator;

import java.io.IOException;
import java.util.Optional;

import static ru.savelying.getdate.utils.UrlUtils.*;

@Slf4j
@WebServlet(LOGIN_URL)
@MultipartConfig
public class LoginControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();
    private final LogValidator validator = LogValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(getJspPath(LOGIN_URL)).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileDTO profileDTO = profileMapper.getProfileDTO(req);
        if (validator.validate(profileDTO).isValid()) {
            Optional<ProfileDTO> userDetailsOptional = profileService.login(profileDTO);
            if (userDetailsOptional.isPresent()) {
                req.getSession().setAttribute("user", userDetailsOptional.get());
                resp.sendRedirect(PROFILE_URL + "?id=" + userDetailsOptional.get().getId());
                log.info("Account login successful with email={} and id={}", profileDTO.getEmail(), profileDTO.getId());
            } else resp.sendRedirect(LOGIN_URL);
        } else {
            req.setAttribute("errors", validator.validate(profileDTO).getErrors());
            req.setAttribute("profile", profileDTO);
            doGet(req, resp);
        }
    }
}
