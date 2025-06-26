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

@Slf4j
@WebServlet("/login")
@MultipartConfig
public class LoginControl extends HttpServlet {
    private final ProfileService profileService = ProfileService.getInstance();
    private final ProfileMapper profileMapper = ProfileMapper.getInstance();
    private final LogValidator validator = LogValidator.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileDTO profileDTO = profileMapper.getProfileDTO(req);
        if (validator.validate(profileDTO).isValid()) {
            Optional<ProfileDTO> userDetailsOptional = profileService.login(profileDTO);
            if (userDetailsOptional.isPresent()) {
                req.getSession().setAttribute("user", userDetailsOptional.get());
                resp.sendRedirect("/profile?id=" + userDetailsOptional.get().getId());
                log.trace("Account login successful with email={} and id={}", profileDTO.getEmail(), profileDTO.getId());
            } else resp.sendRedirect("/login");
        } else {
            req.setAttribute("errors", validator.validate(profileDTO).getErrors());
            req.setAttribute("profile", profileDTO);
            doGet(req, resp);
        }
    }
}
